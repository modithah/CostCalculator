package edu.upc.essi.catalog.optimizer;

import edu.upc.essi.catalog.IO.FileReaderWriter;
import edu.upc.essi.catalog.IO.python.SolverCaller;
import edu.upc.essi.catalog.IO.python.SolverWriter;
import edu.upc.essi.catalog.constants.Const;
import edu.upc.essi.catalog.core.constructs.*;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.metadata.GenerateMetadata;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.query.calculation.QueryCalculator;
import edu.upc.essi.server.WebServer;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());


    public static CostResult calculateCost(HyperGraph graph)  {
//        logger.info("RRRRRRRRRR" + graph.getLocation());
        GenerateMetadata metadataGen = new GenerateMetadata();
        metadataGen.setSizeandMultipliers(graph);

        String wlLocation = WebServer.getWorkload();
//            logger.info(wlLocation);

        if (wlLocation==null)
            wlLocation = Const.WL_LOCATION;
//        ArrayList<Pair<Double, ArrayList<Atom>>> workload = Workload.getWorkload(graph);
//        ArrayList<Pair<Double, ArrayList<Atom>>> workload = Workload.getWorkload2(graph);
        ArrayList<Pair<Double, ArrayList<Atom>>> workload = FileReaderWriter.getWorkload(graph,wlLocation);

        // get query frequencies
        QueryFrequencies freq = QueryCalculator.CalculateFrequency(workload, graph);
        ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies = freq
                .getQueryFrequencies();
        HashMap<Hyperedge, Map<Atom, Double>> global = freq.getGlobalFrequencies();

        // write the dynamic solver
        List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
        HashMap<String, String> elmToString = new HashMap<>();
        MutableDouble collectionSize = new MutableDouble(0.0);
        ArrayList<ArrayList<DataIndexMetadata>> schema = generateSolverData(graph, global, firstLevels, elmToString,
                collectionSize);
        boolean outOfScope = false;
        for (ArrayList<DataIndexMetadata> arr : schema) {
            DataIndexMetadata data = arr.stream().filter(m -> m.isData() == true).findFirst().get();
            if (data.getSizeOrMult() > 4096 * 8) {
                outOfScope = true;
            }
        }


        CostResult result = new CostResult();
        result.setWorkload(workload);
        result.setStorageSize(collectionSize.getValue());
        if (!outOfScope) {
            SolverWriter solver = new SolverWriter();
            solver.generateCode(schema);
            // call the solver and get the miss rates
            JSONObject missRates = SolverCaller.getMissRates();
            if (missRates == null)
                result.AddtoQueryCost(Transformations.getCostMinMax(graph, workload).getSecond());
                // calculate the individual query costs and assemble the result
            else
                calculateQueryCosts(queryFrequencies, firstLevels, elmToString, missRates, result);
        } else {
            result.AddtoQueryCost(Transformations.getCostMinMax(graph, workload).getSecond());
        }
        return result;
    }

    private static void calculateQueryCosts(
            ArrayList<Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>>> queryFrequencies,
            List<Hyperedge> firstLevels, HashMap<String, String> elmToString, JSONObject missRates, CostResult result) {
        try {
            // The order of the workload is the same as of the query frequencies
            for (Pair<ArrayList<Atom>, HashMap<Hyperedge, Map<Atom, Double>>> pairs : queryFrequencies) {

                HashMap<Hyperedge, Map<Atom, Double>> costmap = pairs.getSecond();
                double cost = 0.0;
                for (Hyperedge hyp : firstLevels) {
//					logger.info(hyp.getName());
                    Map<Atom, Double> collectionMap = costmap.get(hyp);

                    cost += collectionMap.get(new Atom("~dummy")) * missRates.getDouble(elmToString.get(hyp.getName()));

//					logger.info("---" + cost);
                    for (Atom at : collectionMap.keySet()) {
                        if (!at.getName().equals("~dummy")
                                && elmToString.containsKey(hyp.getName() + "~" + at.getName())) {
//							logger.info(at);
//							logger.info(elmToString.get(hyp.getName() + "~" + at.getName()));
                            cost += collectionMap.get(at)
                                    * missRates.getDouble(elmToString.get(hyp.getName() + "~" + at.getName()));
//							logger.info("+++"+cost);
                        }

                    }
                }

                result.AddtoQueryCost(cost);

            }
        } catch (JSONException e) {
            // TODO here is the error
            result.AddtoQueryCost(9999);
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static ArrayList<ArrayList<DataIndexMetadata>> generateSolverData(HyperGraph graph,
                                                                              HashMap<Hyperedge, Map<Atom, Double>> global, List<Hyperedge> firstLevels,
                                                                              HashMap<String, String> elmToString, MutableDouble collectionSize) {
        ArrayList<ArrayList<DataIndexMetadata>> schema = new ArrayList<>();
        ArrayList<Hyperedge> notUsed = new ArrayList<>();

        int collectionCounter = 0;

        for (Hyperedge hyperedge : firstLevels) {

            collectionSize.add(hyperedge.getSize());
//			logger.info("FFFFFFFFFFFFFFFFFFFF" + hyperedge.getSize() + "    "+ hyperedge.getName());
            if (global.get(hyperedge).get(new Atom("~dummy")) != 0) {
                collectionCounter++;
                String collectionPrefix = "collection_" + collectionCounter;
                elmToString.put(hyperedge.getName(), collectionPrefix);
                ArrayList<DataIndexMetadata> col = new ArrayList<>();

//				logger.info(hyperedge);

                double hyperedgecount = 0.0;
                List<HGHandle> secondLevels = hyperedge.findAll();

                if (secondLevels.size() == 1) {
                    int indexCount = 1;
                    Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevels.get(0));
                    Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
                    hyperedgecount = hyperedge.getMultipliers().get(secondLevelRoot);
                    if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//						logger.info(global.get(hyperedge).get(secondLevelRoot));
                        col.add(new DataIndexMetadata(false, 1, hyperedgecount,
                                global.get(hyperedge).get(secondLevelRoot), DataIndexMetadata.DataType.UUID));

                        elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
                                collectionPrefix + "_index_" + indexCount);
                        indexCount++;
                    }

                    for (Atom atom : global.get(hyperedge).keySet()) {
                        if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
                                && global.get(hyperedge).get(atom) != 0) {
//						logger.info(hyperedge.getMultipliers().get(atom));
                            col.add(new DataIndexMetadata(false, hyperedge.getMultipliers().get(atom) / hyperedgecount,
                                    hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
                                    DataIndexMetadata.DataType.INT));
                            elmToString.put(hyperedge.getName() + "~" + atom.getName(),
                                    collectionPrefix + "_index_" + indexCount);
//							logger.info(collectionPrefix+"_index_"+indexCount);
                            indexCount++;
                        }
                    }
                } else {
                    logger.info("XXXXXXXXXXXXXXXXXXXXXXXX");
//                	logger.info(global);
                    int indexCount = 1;
                    for (HGHandle secondLevel : secondLevels) {
                        Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevel);
                        Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
                        hyperedgecount += hyperedge.getMultipliers().get(secondLevelRoot);
                    }

                    logger.info("GGGGGGGGGGGGGGGG  " + hyperedgecount);
                    for (HGHandle secondLevel : secondLevels) {

                        Hyperedge secondlevelHyperedge = (Hyperedge) graph.get(secondLevel);
                        Atom secondLevelRoot = graph.get(secondlevelHyperedge.getRoot());
//                        hyperedgecount += hyperedge.getMultipliers().get(secondLevelRoot);
                        if (global.get(hyperedge).get(secondLevelRoot) != 0) {
//    						logger.info(global.get(hyperedge).get(secondLevelRoot));
//                            col.add(new DataIndexMetadata(false, 1, hyperedgecount,
//                                    global.get(hyperedge).get(secondLevelRoot), DataIndexMetadata.DataType.UUID));

                            elmToString.put(hyperedge.getName() + "~" + secondLevelRoot.getName(),
                                    collectionPrefix + "_index_" + indexCount);
                            indexCount++;
                        }

                        for (Atom atom : global.get(hyperedge).keySet()) {
                            if (!atom.getName().equals("~dummy") && !atom.getName().equals(secondLevelRoot.getName())
                                    && global.get(hyperedge).get(atom) != 0) {
//    						logger.info(hyperedge.getMultipliers().get(atom));
                                col.add(new DataIndexMetadata(false,
                                        hyperedge.getMultipliers().get(atom) / hyperedgecount,
                                        hyperedge.getMultipliers().get(atom), global.get(hyperedge).get(atom),
                                        DataIndexMetadata.DataType.INT));
                                elmToString.put(hyperedge.getName() + "~" + atom.getName(),
                                        collectionPrefix + "_index_" + indexCount);
                                logger.info(collectionPrefix + "_index_" + indexCount);
                                indexCount++;
                            }
                        }

                    }
                }

                col.add(new DataIndexMetadata(true, hyperedge.getSize() / hyperedgecount, hyperedgecount,
                        global.get(hyperedge).get(new Atom("~dummy")), DataIndexMetadata.DataType.DATA));
//				logger.info(col);
                schema.add(col);
            } else {
                notUsed.add(hyperedge);
            }
        }
        firstLevels.removeAll(notUsed);
        return schema;
    }

}
