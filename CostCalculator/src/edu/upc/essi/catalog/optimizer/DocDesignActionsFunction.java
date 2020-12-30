package edu.upc.essi.catalog.optimizer;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.ops.Transformations;
import edu.upc.essi.catalog.optimizer.actions.*;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Sets;
import org.hypergraphdb.HyperGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class DocDesignActionsFunction implements ActionsFunction {
    MessageDigest messageDigest;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Action> actions(Object o) {
        HyperGraph G = (HyperGraph) o;
        Set<Action> actions = Sets.newHashSet();

        String stringHash = new String(messageDigest.digest());
        // Union
        Transformations.getUnionCandidates(G).forEach(uCandidate -> {
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            logger.info("union");
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.union(dummy,uCandidate.getFirst(), uCandidate.getSecond());
            messageDigest.reset();
            messageDigest.update(Graphoperations.stringDesign(dummy).getBytes());
            String hash = new String(messageDigest.digest());
            if (!WorkflowExecutions.usedConfigurations.contains(hash)) {
                WorkflowExecutions.usedConfigurations.add(hash);
                actions.add(new UnionAction(newG, uCandidate.getFirst(), uCandidate.getSecond()));
                logger.info("union added");
            }else {
                newG.close();
                try {
                    FileUtils.deleteDirectory(new File(newG.getLocation()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dummy.close();
            try {
                FileUtils.deleteDirectory(new File(dummy.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Flatten
        Transformations.getFlattenCandidates(G).forEach(fCandidate -> {
            logger.info("flatten");
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.flatten(dummy,fCandidate);
            messageDigest.update(Graphoperations.stringDesign(dummy).getBytes());
            String hash = new String(messageDigest.digest());
            messageDigest.reset();
            if (!WorkflowExecutions.usedConfigurations.contains(hash)) {
                WorkflowExecutions.usedConfigurations.add(hash);
                actions.add(new FlattenAction(newG,fCandidate));
                logger.info("flatten added");
            }else {
                newG.close();
                try {
                    FileUtils.deleteDirectory(new File(newG.getLocation()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dummy.close();
            try {
                FileUtils.deleteDirectory(new File(dummy.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //Group
        Transformations.getGroupCandidates(G).forEach(gCandidate -> {
            logger.info("group");
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.group(dummy,gCandidate);
            messageDigest.reset();
            messageDigest.update(Graphoperations.stringDesign(dummy).getBytes());
            String hash = new String(messageDigest.digest());
            if (!WorkflowExecutions.usedConfigurations.contains(hash)) {
                WorkflowExecutions.usedConfigurations.add(hash);
                actions.add(new GroupAction(newG,gCandidate));
                logger.info("group added");

            }else {
                newG.close();
                try {
                    FileUtils.deleteDirectory(new File(newG.getLocation()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dummy.close();
            try {
                FileUtils.deleteDirectory(new File(dummy.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Embed
        Transformations.getEmbedCandidates(G).forEach(eCandidate -> {
            logger.info("embed");
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.embed(dummy,eCandidate);
            messageDigest.reset();
            messageDigest.update(Graphoperations.stringDesign(dummy).getBytes());
            String hash = new String(messageDigest.digest());
            if (!WorkflowExecutions.usedConfigurations.contains(hash)) {
                WorkflowExecutions.usedConfigurations.add(hash);
                actions.add(new EmbedAction(newG,eCandidate));
                logger.info("embed added");
            }else {
                newG.close();
                try {
                    FileUtils.deleteDirectory(new File(newG.getLocation()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dummy.close();
            try {
                FileUtils.deleteDirectory(new File(dummy.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //Segregate
        Transformations.getSegregateCandidates(G).forEach(sCandidate -> {
            logger.info("segregate");
            //Create a copy
            HyperGraph newG = Graphoperations.makeGraphCopy(G);
            //Apply the transformation to check if its already tested
            HyperGraph dummy = Graphoperations.makeGraphCopy(G);
            Transformations.segregate(dummy,sCandidate.getFirst(),sCandidate.getSecond());
            messageDigest.reset();
            messageDigest.update(Graphoperations.stringDesign(dummy).getBytes());
            String hash = new String(messageDigest.digest());
            if (!WorkflowExecutions.usedConfigurations.contains(hash)) {
                logger.info("segregate added");
                WorkflowExecutions.usedConfigurations.add(hash);
                actions.add(new SegregateAction(newG,sCandidate.getFirst(),sCandidate.getSecond()));
            }
            else {
                newG.close();
                try {
                    FileUtils.deleteDirectory(new File(newG.getLocation()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dummy.close();
            try {
                FileUtils.deleteDirectory(new File(dummy.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return actions;
    }

}
