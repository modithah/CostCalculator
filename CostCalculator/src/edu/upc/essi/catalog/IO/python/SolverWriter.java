package edu.upc.essi.catalog.IO.python;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.Type;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import edu.upc.essi.catalog.core.constructs.DataIndexMetadata;
import edu.upc.essi.catalog.core.constructs.DataIndexMetadata.DataType;

public class SolverWriter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<DataIndexMetadata>> schema = new ArrayList<>();
		ArrayList<DataIndexMetadata> col1 = new ArrayList<>();
		col1.add(new DataIndexMetadata(true, 244.5f, 2500000, 0.296718017f, DataType.DATA));
		col1.add(new DataIndexMetadata(false, 1, 2500000, 0.066979236f, DataType.UUID));
		col1.add(new DataIndexMetadata(false, 5.7f, 4000000, 0.066979236f, DataType.INT));

		ArrayList<DataIndexMetadata> col2 = new ArrayList<>();
		col2.add(new DataIndexMetadata(true, 265, 4000000, 0.435365037f, DataType.UUID));
		col2.add(new DataIndexMetadata(false, 1, 4000000, 0.066979236f, DataType.UUID));
		col2.add(new DataIndexMetadata(false, 3.5f, 2500000, 0.066979236f, DataType.INT));
		schema.add(col1);
		schema.add(col2);

		ArrayList<String> collections = new ArrayList<>();
		ArrayList<String> primaryIndexes = new ArrayList<>();
		ArrayList<String> secondaryIndexes = new ArrayList<>();
		float memory = 213466880.3f;
		try {

			PrintStream writer = new PrintStream(new File("C:\\Users\\Moditha\\Desktop\\ADBIS\\solve.py"));
			writeFixedContent(writer);
			writer.println("# Memory");
			writer.println("M=  " + memory);
			writer.println();

			writeCollectionIndexData(schema, collections, primaryIndexes, secondaryIndexes, writer);

			writer.println();
			writeSaturationSoverVariables(collections, primaryIndexes, secondaryIndexes, writer);

			writer.println("m.Equations([");

			writeMemorySum(collections, primaryIndexes, secondaryIndexes, writer, false);
			writer.println();

			writeBlocksToSize(collections, primaryIndexes, secondaryIndexes, writer);
			writer.println();
			writeProbabilityEqs(collections, primaryIndexes, secondaryIndexes, writer);
			writer.println();
			writeReqSfExp(primaryIndexes, secondaryIndexes, writer);

			initializeEvictionVariables(collections, primaryIndexes, secondaryIndexes, writer);

			writer.println("j.Equations([");
			writeMemorySum(collections, primaryIndexes, secondaryIndexes, writer, true);

			collections.stream().forEach(string -> {
				writer.println(
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});
			primaryIndexes.stream().forEach(string -> {
				writer.println(
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});
			secondaryIndexes.stream().forEach(string -> {
				writer.println(
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});

			writer.println();
			ArrayList<String> allEs = new ArrayList<>();
			ArrayList<String> allPs = new ArrayList<>();

			collections.stream().forEach(string -> {
				writer.println(string + "_E == " + string + "_evict * dB * (1 - 1 / (" + string + "_evict * dB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
//				P1 * (1-mda1/dleaf1)
			});
			primaryIndexes.stream().forEach(string -> {
				writer.println(string + "_E == " + string + "_evict * iB * (1 - 1 / (" + string + "_evict * iB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
			});
			secondaryIndexes.stream().forEach(string -> {
				writer.println(string + "_E == " + string + "_evict * iB * (1 - 1 / (" + string + "_evict * iB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
			});

			for (int i = 0; i < allEs.size(); i++) {
				writer.println(allEs.get(i) + " / (" + String.join("+", allEs) + ")==");
				writer.println(allPs.get(i) + " / (" + String.join("+", allPs) + "),");
			}

			writer.println("]) ");
			writer.println("j.options.SOLVER = 1 ");
			writer.println("j.solve() ");
			writer.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void initializeEvictionVariables(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, PrintStream writer) {
		writer.println("j = GEKKO(remote=False)");
		writer.println("Q1=T.value[0]");
		writer.println("\n");
		writer.println("# Initialize variables \n");

		collections.stream().forEach(string -> {
			writer.println(string + "_evict = j.Var(value = " + string + "_sat.value[0] / dB)");
			writer.println(string + "_E = j.Var(value = " + string + "_sat.value[0] / dB)");
			writer.println(string + "_shots = j.Var(value=1000)");
		});

		writer.println();

		primaryIndexes.stream().forEach(string -> {
			writer.println(string + "_evict = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(string + "_E = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(string + "_shots = j.Var(value=1000)");
		});
		writer.println();

		secondaryIndexes.stream().forEach(string -> {
			writer.println(string + "_evict = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(string + "_E = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(string + "_shots = j.Var(value=1000)");
		});
	}

	private static void writeReqSfExp(ArrayList<String> primaryIndexes, ArrayList<String> secondaryIndexes,
			PrintStream writer) {
		primaryIndexes.forEach(string -> {
			writer.println(string + "_SF == " + string + "_exp / " + string.split("_index")[0] + "_count,");
		});
		secondaryIndexes.forEach(string -> {
			writer.println(string + "_SF == " + string + "_exp / " + string.split("_index")[0] + "_count,");
		});
		writer.println();
		primaryIndexes.forEach(string -> {
			writer.println(string + "_req == T * " + string + "_freq, ");
		});
		secondaryIndexes.forEach(string -> {
			writer.println(string + "_req == T * " + string + "_freq, ");
		});
		writer.println();
		primaryIndexes.forEach(string -> {
			String count = string.split("_index")[0] + "_count";
			writer.println(string + "_exp == " + count + " * ( 1 - ((" + count + "- 1) /" + count + ") ** " + string
					+ "_req),");
		});
		secondaryIndexes.forEach(string -> {
			String distict = string + "_distinct";
			writer.println(string + "_exp == " + distict + " * ( 1 - ((" + distict + "- 1) /" + distict + ") ** "
					+ string + "_req),");
		});

		writer.println("]) ");

		writer.println("m.options.SOLVER = 1");
		writer.println("m.solve(disp=False,debug=False)");
		writer.println("\n\n");
	}

	private static void writeBlocksToSize(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, PrintStream writer) {
		collections.stream().forEach(string -> {
			writer.println(string + "_sat == " + string + "_leaf * dB * " + string + "_prob,");
		});
		primaryIndexes.stream().forEach(string -> {
			writer.println(string + "_sat == " + string + "_leaf * iB * " + string + "_prob,");
		});
		secondaryIndexes.stream().forEach(string -> {
			writer.println(string + "_sat == " + string + "_leaf * iB * " + string + "_prob,");
		});
	}

	private static void writeCollectionIndexData(ArrayList<ArrayList<DataIndexMetadata>> schema,
			ArrayList<String> collections, ArrayList<String> primaryIndexes, ArrayList<String> secondaryIndexes,
			PrintStream writer) {
		int collectionCounter = 1;
		for (ArrayList<DataIndexMetadata> col : schema) {
			DataIndexMetadata data = col.stream().filter(m -> m.isData() == true).findFirst().get();
			float docCount = data.getDistinctCount();
			float docSize = data.getSizeOrMult();
			int indexCounter = 1;
			String collectionPrefix = "collection_" + collectionCounter;
			collections.add(collectionPrefix);
			writer.println("# Collection " + collectionPrefix);
			writer.println(collectionPrefix + "_size = " + docSize);
			writer.println(collectionPrefix + "_count = " + docCount);
			writer.println(collectionPrefix + "_freq = " + data.getFrequency());
			writer.println("#docs per block");
			writer.println(collectionPrefix + "_docs = math.floor((dfill*dB-dmeta) / " + collectionPrefix + "_size)");

			writer.println("#data leaf and internal blocks");
			writer.println(
					collectionPrefix + "_leaf  = " + collectionPrefix + "_count / " + collectionPrefix + "_docs");
			writer.println(collectionPrefix + "_internal  = " + collectionPrefix
					+ "_leaf * dinternal / ( dfillint * dBint - dmeta)");
//				
			writer.println();
			writer.println("# Collection " + collectionPrefix + " Indexes #####");
			for (DataIndexMetadata meta : col) {
				if (!meta.isData()) {
					String indexPrefix = collectionPrefix + "_index_" + indexCounter;
					writer.println("# Index " + indexPrefix);
					writer.println();
					writer.println(indexPrefix + "_mult = " + meta.getSizeOrMult());
					writer.println(indexPrefix + "_distinct = " + meta.getDistinctCount());
					writer.println(indexPrefix + "_rep = " + indexPrefix + "_mult * " + collectionPrefix + "_count / "
							+ indexPrefix + "_distinct");
					writer.println(indexPrefix + "_freq = " + meta.getFrequency());
					indexCounter++;
					writer.println();
					writer.println("# Index " + indexPrefix + " leaf and internal blocks");

					switch (meta.getDataType()) {
					case UUID:
						writer.println(indexPrefix + "_leaf = " + collectionPrefix + "_count / primary_idx");
						writer.println(indexPrefix + "_internal = " + indexPrefix
								+ "_leaf * iinternal / (ifillint * iBint - imeta)");
						primaryIndexes.add(indexPrefix);
						break;
					case INT:
						writer.println(indexPrefix + "_leaf = " + indexPrefix + "_mult * " + collectionPrefix
								+ "_count / secondary_idx");
						writer.println(indexPrefix + "_internal = " + indexPrefix
								+ "_leaf * iinternal / (ifillint * iBint - imeta)");
						secondaryIndexes.add(indexPrefix);
						break;

					default:
						break;
					}
					writer.println();
				}
			}

			collectionCounter++;
		}
	}

	private static void writeSaturationSoverVariables(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, PrintStream writer) {
		writer.println("# saturation memory variables");

		writer.println("T = m.Var(value = 5000)");
//			Pd1 = m.Var(value=1)

		for (String string : collections) {
			writer.println(string + "_sat = m.Var(value = 1000)");
			writer.println(string + "_prob = m.Var(value = 1)");

		}
		for (String string : primaryIndexes) {
			writer.println(string + "_sat = m.Var(value = 1000)");
			writer.println(string + "_req = m.Var(value = 1)");
			writer.println(string + "_SF = m.Var(value = 1)");
			writer.println(string + "_exp = m.Var(value = 1)");
			writer.println(string + "_prob = m.Var(value = 1)");
		}
		for (String string : secondaryIndexes) {
			writer.println(string + "_sat = m.Var(value = 1000)");
			writer.println(string + "_req = m.Var(value = 1)");
			writer.println(string + "_SF = m.Var(value = 1)");
			writer.println(string + "_exp = m.Var(value = 1)");
			writer.println(string + "_prob = m.Var(value = 1)");
		}
	}

	private static void writeProbabilityEqs(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, PrintStream writer) {
		StringBuilder sb;
		for (String string : collections) {
			writer.println(string + "_prob == ");
			ArrayList<String> indexes = new ArrayList<>();
			primaryIndexes.stream().filter(s -> s.contains(string)).forEach(idx -> {
				indexes.add(idx + "_SF");
			});
			secondaryIndexes.stream().filter(s -> s.contains(string)).forEach(idx -> {
				indexes.add("( 1 - (1 - " + idx + "_SF) ** " + idx + "_mult)");
			});

			sb = new StringBuilder();
			sb.append("1 - (1 - ((");
			for (int i = 1; i < indexes.size() + 1; i++) {
				Set<Set<String>> combos = Sets.combinations(ImmutableSet.copyOf(indexes), i);
				Iterator<Set<String>> comboIterator = combos.iterator();
				int j = 0;
				while (comboIterator.hasNext()) {
					Set<java.lang.String> set = (Set<java.lang.String>) comboIterator.next();
					if (!(i == 1 && j == 0))
						sb.append(Math.pow(-1, i + 1) > 0 ? "+ (" : "-(");
					else
						sb.append("(");
					sb.append(String.join("*", set));
					sb.append(")\n");
					j++;
				}

			}
			sb.append(")/1 )) ** " + string + "_docs ,\n");
			writer.print(sb.toString());
		}

		primaryIndexes.stream().forEach(i -> {
			writer.println(i + "_prob == 1 - ( 1 - " + i + "_SF) ** primary_idx ,");
		});

		secondaryIndexes.stream().forEach(i -> {
			writer.println(i + "_prob == 1 - ( 1 - " + i + "_SF) ** (secondary_idx /" + i + "_rep) ,");
		});
	}

	private static void writeMemorySum(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, PrintStream writer, boolean eviction) {
		StringBuilder sb = new StringBuilder();
		for (String string : collections) {
			sb.append("1.1 * (" + string + (eviction ? "_evict*dB + " : "_sat + ") + string + "_internal * dBint) +\n");
		}
		for (String string : primaryIndexes) {
			sb.append("1.1 * (" + string + (eviction ? "_evict*iB + " : "_sat + ") + string + "_internal * iBint) +\n");
		}
		for (String string : secondaryIndexes) {
			sb.append("1.1 * (" + string + (eviction ? "_evict*iB + " : "_sat + ") + string + "_internal * iBint) +\n");
		}
		sb.deleteCharAt(sb.lastIndexOf("+"));
		sb.deleteCharAt(sb.lastIndexOf("\n"));
		sb.append(" == M,\n");
		writer.print(sb.toString());
		writer.println();
	}

	private static void writeFixedContent(PrintStream writer) {
		writer.println("### Fixed data ######");

		writer.println("from gekko import GEKKO");
		writer.println("import math");
		writer.println("m = GEKKO(remote=False)  # create GEKKO model");
		writer.println("#data");
		writer.println("dinternal=22");
		writer.println("idsize=12");
		writer.println("dmeta=6");
		writer.println("dB = 4096*8");
		writer.println("dfill=1");
		writer.println("dBint = 4096");
		writer.println("dfillint=.75");
		writer.println("\n\n");
		writer.println("#index");
		writer.println("iinternal=22");
		writer.println("imeta=30");
		writer.println("iB = 4096*8");
		writer.println("ifill=1");
		writer.println("iBint = 4096*4");
		writer.println("ifillint=.75");
//		writer.println("i=15");
		writer.println("primary_idx = math.floor((ifill*iB-imeta) / 15)");
		writer.println("secondary_idx = math.floor((ifill*iB-imeta) / 8)");
	}

}
