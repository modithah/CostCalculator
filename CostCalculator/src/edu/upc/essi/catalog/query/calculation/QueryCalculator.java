package edu.upc.essi.catalog.query.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Element;
import edu.upc.essi.catalog.core.constructs.Hyperedge;
import edu.upc.essi.catalog.enums.AtomTypeEnum;
import edu.upc.essi.catalog.ops.Graphoperations;

public class QueryCalculator {

	public QueryCalculator() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<Hyperedge, Map<Atom, Double>> CalculateFrequency(ArrayList<Pair<Double, ArrayList<Atom>>> workload, HyperGraph graph) {
		List<Hyperedge> firstLevels = Graphoperations.getAllFirstLevels(graph);
		HashMap<Hyperedge, Map<Atom, Double>> globalfrequencies = new HashMap<Hyperedge, Map<Atom, Double>>();
		Atom dummy = new Atom("~dummy");
		initializeFreqMap(firstLevels, globalfrequencies, dummy);

		for (Pair<Double, ArrayList<Atom>> pair : workload) {

			HashMap<Hyperedge, Map<Atom, Double>> frequencies = new HashMap<Hyperedge, Map<Atom, Double>>();
			initializeFreqMap(firstLevels, frequencies, dummy);
			Queue<Hyperedge> winners = new LinkedList<>();
			double freq = pair.getFirst();
			ArrayList<Atom> remaining = pair.getSecond();
			List<Hyperedge> candidatesWithRoot = firstLevels.stream()
					.filter(h -> h.getMultipliers().containsKey(remaining.get(0))).collect(Collectors.toList());
			SizeComparator sc = new SizeComparator();
			Collections.sort(candidatesWithRoot, sc);
			if (candidatesWithRoot.size() >= 1) { // get the smallest or the only collection can change to biggest
													// intersection
				winners.add(candidatesWithRoot.get(0));
			} else if (candidatesWithRoot.size() == 0) { // no collection with the selection as the root
				HashMap<Hyperedge, Set> intersections = new HashMap<>();
				firstLevels.stream().forEach(f -> {
					Set<Atom> set = new HashSet<>();
					set.addAll(f.getMultipliers().keySet());
					set.retainAll(remaining);
					intersections.put(f, set);
				});

				boolean firstRound = true;

				Hyperedge winner = null;
				// currently biggeest intersection can change to size
				for (Hyperedge hyperedge : intersections.keySet()) {
					if (firstRound) {
						winner = hyperedge;
						firstRound = false;
					} else if (intersections.get(winner).size() < intersections.get(hyperedge).size()) {
						winner = hyperedge;
					}
				}
				winners.add(winner);
			}

			if (!winners.isEmpty()) {
				Hyperedge first = winners.peek();
				Map<Atom, Double> entry = frequencies.get(first);
				
				double colValue = entry.get(dummy);
				double keyValue = entry.get(remaining.get(0));
				keyValue = keyValue + freq;
				int rootEntries = ((Atom) graph.get(((Hyperedge) graph.get(first.findAll().get(0))).getRoot())).getCount(); // need to change for multiple entries
				colValue = colValue + freq * first.getMultipliers().get(remaining.get(0)) / rootEntries;
				entry.put(dummy, colValue);
				entry.put(remaining.get(0), keyValue);
				frequencies.put(first, entry);

				while (!winners.isEmpty() && !remaining.isEmpty()) {
					System.out.println(remaining);
					Hyperedge main = winners.poll();
					System.out.println(main);
					Set<Atom> covered = new HashSet<>();
					covered.addAll(main.getMultipliers().keySet());
					covered.retainAll(remaining);
					remaining.removeAll(covered);

					Iterator<Atom> it = covered.iterator();
					ArrayList<Atom> alraedyDone= new ArrayList<>();
					while (it.hasNext()) {
						
						Atom atm = it.next(); // parent node that is connected
//						if (atm.getType() == AtomTypeEnum.Attribute) {  // if its a join Class is in the parent already and  attribute is in the 
						for (Atom rem : remaining) { // joined collection having also the class
							if(!alraedyDone.contains(rem)) {
							List<Hyperedge> candidates = firstLevels.stream().filter(
									h -> h.getMultipliers().containsKey(rem) && h.getMultipliers().containsKey(atm))
									.collect(Collectors.toList());
							Collections.sort(candidates, sc);
							if (candidates.size() > 0) {
								first = candidates.get(0); // to hyperedge
								entry = frequencies.get(first);
								Map<Atom, Double> rootEntry = frequencies.get(main);
								colValue = entry.get(dummy);
								keyValue = entry.get(atm); // you change the connected one atm exists in both parent and
															// child to join
								keyValue = keyValue + rootEntry.get(dummy); // key is accessed the same frequency as the
																			// parent collection
								rootEntries = ((Atom) graph.get(((Hyperedge) graph.get(first.findAll().get(0))).getRoot())).getCount(); // need to change for multiple entries
								
										
										//((Atom) graph.get(first.getRoot())).getCount();
								colValue = colValue
										+ rootEntry.get(dummy) * first.getMultipliers().get(atm) / rootEntries;
								entry.put(dummy, colValue);
								entry.put(atm, keyValue);
								frequencies.put(first, entry);
								winners.add(first);
								alraedyDone.addAll(first.getMultipliers().keySet());
								System.out.println("done" +alraedyDone);
							}
						}
						}
//						}

					}

				}

				for (Hyperedge hyperedge : frequencies.keySet()) {
					Map<Atom, Double> localCol = frequencies.get(hyperedge);
					Map<Atom, Double> globalCol = globalfrequencies.get(hyperedge);
					for (Atom atom : localCol.keySet()) {
						Double localVal = localCol.get(atom);
						Double globalval = globalCol.get(atom);
						globalval = globalval + localVal;
						globalCol.put(atom, globalval);
					}
					globalfrequencies.put(hyperedge, globalCol);
				}

			} else {
				System.out.println("cant answer query");
			}

		}
		return globalfrequencies;

	}

	private static void initializeFreqMap(List<Hyperedge> firstLevels, HashMap<Hyperedge, Map<Atom, Double>> frequencies,
			Atom dummy) {
		for (Hyperedge hyperedge : firstLevels) {
			HashMap<Atom, Double> map = new HashMap<Atom, Double>();

			map.put(dummy, 0.0);
			for (Atom atom : hyperedge.getMultipliers().keySet()) {
				map.put(atom, 0.0);
			}
			frequencies.put(hyperedge, map);
		}
	}

}
