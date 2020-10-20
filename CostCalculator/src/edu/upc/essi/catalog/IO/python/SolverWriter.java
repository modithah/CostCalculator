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

import edu.upc.essi.catalog.IO.TabbedPrintStream;
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

//		generateCode(schema);
	}

	public void generateCode(ArrayList<ArrayList<DataIndexMetadata>> schema) {
		ArrayList<String> collections = new ArrayList<>();
		ArrayList<String> primaryIndexes = new ArrayList<>();
		ArrayList<String> secondaryIndexes = new ArrayList<>();
		float memory = 213466880.3f;
		try {

//			"C:\\Users\\Moditha\\Desktop\\ADBIS\\solve.py"
			TabbedPrintStream writer = new TabbedPrintStream(new File("data/python/solve.py"));
			writeFixedContent(writer);
			writer.println(1, "# Memory");
			writer.println(1, "M=  " + memory);
			writer.println();

			writeCollectionIndexData(schema, collections, primaryIndexes, secondaryIndexes, writer);

			writer.println();
			writeSaturationSoverVariables(collections, primaryIndexes, secondaryIndexes, writer);

			writer.println(1, "m.Equations([");

			writeMemorySum(collections, primaryIndexes, secondaryIndexes, writer, false);
			writer.println();

			writeBlocksToSize(collections, primaryIndexes, secondaryIndexes, writer);
			writer.println();
			writeProbabilityEqs(collections, primaryIndexes, secondaryIndexes, writer);
			writer.println();
			writeReqSfExp(primaryIndexes, secondaryIndexes, writer);

			initializeEvictionVariables(collections, primaryIndexes, secondaryIndexes, writer);

			writer.println(1, "j.Equations([");
			writeMemorySum(collections, primaryIndexes, secondaryIndexes, writer, true);

			collections.stream().forEach(string -> {
				writer.println(1,
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});
			primaryIndexes.stream().forEach(string -> {
				writer.println(1,
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});
			secondaryIndexes.stream().forEach(string -> {
				writer.println(1,
						string + "_shots == " + string + "_freq * Q1 * " + string + "_evict / " + string + "_leaf,");
			});

			writer.println();
			ArrayList<String> allEs = new ArrayList<>();
			ArrayList<String> allPs = new ArrayList<>();

			collections.stream().forEach(string -> {
				writer.println(1, string + "_E == " + string + "_evict * dB * (1 - 1 / (" + string + "_evict * dB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
//				P1 * (1-mda1/dleaf1)
			});
			primaryIndexes.stream().forEach(string -> {
				writer.println(1, string + "_E == " + string + "_evict * iB * (1 - 1 / (" + string + "_evict * iB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
			});
			secondaryIndexes.stream().forEach(string -> {
				writer.println(1, string + "_E == " + string + "_evict * iB * (1 - 1 / (" + string + "_evict * iB)) ** "
						+ string + "_shots,");
				allEs.add(string + "_E");
				allPs.add(string + "_freq * (1- " + string + "_evict / " + string + "_leaf)");
			});

			for (int i = 0; i < allEs.size(); i++) {
				writer.println(1, allEs.get(i) + " / (" + String.join("+", allEs) + ")==");
				writer.println(1, allPs.get(i) + " / (" + String.join("+", allPs) + "),");
			}

			writer.println(1, "]) ");
			writer.println(1, "j.options.SOLVER = 1 ");
			writer.println(1, "j.solve() ");
//			writer.println(1, "j.solve() ");
			writer.println(1, "dict = {} ");
			collections.stream().forEach(s -> {
				writer.println(1, "dict['" + s + "'] = 1 - " + s + "_evict.value[0] / " + s + "_leaf");
//				1 - collection_1_evict.value[0] / collection_1_leaf,
			});
			primaryIndexes.stream().forEach(s -> {
				writer.println(1, "dict['" + s + "'] = 1 - " + s + "_evict.value[0] / " + s + "_leaf");
			});
			secondaryIndexes.stream().forEach(s -> {
				writer.println(1, "dict['" + s + "'] = 1 - " + s + "_evict.value[0] / " + s + "_leaf");
			});
			
			int array[] = {1,2,3,4};
			
			//new ArrayList<>(array);

//			writer.println(1, "for variable in lst :");
//			writer.println(2, "dict[variable] = eval(variable)");
			writer.println();
			writer.println(1, "return dict");

//			for variable in ["name", "age", "height"]:
//
//			    a_dict[variable] = eval(variable)
//			list = ['larry', 'curly', 'moe']
//			  list.append('shemp')
			writer.close();
//			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void initializeEvictionVariables(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, TabbedPrintStream writer) {
		writer.println(1, "j = GEKKO(remote=False)");
		writer.println(1, "Q1=T.value[0]");
		writer.println(1, "\n");
		writer.println(1, "# Initialize variables \n");

		collections.stream().forEach(string -> {
			writer.println(1, string + "_evict = j.Var(value = " + string + "_sat.value[0] / dB)");
			writer.println(1, string + "_E = j.Var(value = " + string + "_sat.value[0] / dB)");
			writer.println(1, string + "_shots = j.Var(value=1000)");
		});

		writer.println();

		primaryIndexes.stream().forEach(string -> {
			writer.println(1, string + "_evict = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(1, string + "_E = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(1, string + "_shots = j.Var(value=1000)");
		});
		writer.println();

		secondaryIndexes.stream().forEach(string -> {
			writer.println(1, string + "_evict = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(1, string + "_E = j.Var(value = " + string + "_sat.value[0] / iB)");
			writer.println(1, string + "_shots = j.Var(value=1000)");
		});
	}

	private static void writeReqSfExp(ArrayList<String> primaryIndexes, ArrayList<String> secondaryIndexes,
			TabbedPrintStream writer) {
		primaryIndexes.forEach(string -> {
			writer.println(1, string + "_SF == " + string + "_exp / " + string.split("_index")[0] + "_count,");
		});
		secondaryIndexes.forEach(string -> {
			writer.println(1, string + "_SF == " + string + "_exp / " + string.split("_index")[0] + "_count,");
		});
		writer.println();
		primaryIndexes.forEach(string -> {
			writer.println(1, string + "_req == T * " + string + "_freq, ");
		});
		secondaryIndexes.forEach(string -> {
			writer.println(1, string + "_req == T * " + string + "_freq, ");
		});
		writer.println();
		primaryIndexes.forEach(string -> {
			String count = string.split("_index")[0] + "_count";
			writer.println(1, string + "_exp == " + count + " * ( 1 - ((" + count + "- 1) /" + count + ") ** " + string
					+ "_req),");
		});
		secondaryIndexes.forEach(string -> {
			String distict = string + "_distinct";
			writer.println(1, string + "_exp == " + distict + " * ( 1 - ((" + distict + "- 1) /" + distict + ") ** "
					+ string + "_req),");
		});

		writer.println(1, "]) ");

		writer.println(1, "m.options.SOLVER = 1");
		writer.println(1, "m.solve(disp=False,debug=False)");
		writer.println(1, "\n\n");
	}

	private static void writeBlocksToSize(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, TabbedPrintStream writer) {
		collections.stream().forEach(string -> {
			writer.println(1, string + "_sat == " + string + "_leaf * dB * " + string + "_prob,");
		});
		primaryIndexes.stream().forEach(string -> {
			writer.println(1, string + "_sat == " + string + "_leaf * iB * " + string + "_prob,");
		});
		secondaryIndexes.stream().forEach(string -> {
			writer.println(1, string + "_sat == " + string + "_leaf * iB * " + string + "_prob,");
		});
	}

	private static void writeCollectionIndexData(ArrayList<ArrayList<DataIndexMetadata>> schema,
			ArrayList<String> collections, ArrayList<String> primaryIndexes, ArrayList<String> secondaryIndexes,
			TabbedPrintStream writer) {
		int collectionCounter = 1;
		for (ArrayList<DataIndexMetadata> col : schema) {
			DataIndexMetadata data = col.stream().filter(m -> m.isData() == true).findFirst().get();
			double docCount = data.getDistinctCount();
			double docSize = data.getSizeOrMult();
			int indexCounter = 1;
			String collectionPrefix = "collection_" + collectionCounter;
			collections.add(collectionPrefix);
			writer.println(1, "# Collection " + collectionPrefix);
			writer.println(1, collectionPrefix + "_size = " + docSize);
			writer.println(1, collectionPrefix + "_count = " + docCount);
			writer.println(1, collectionPrefix + "_freq = " + data.getFrequency());
			writer.println(1, "#docs per block");
			writer.println(1,
					collectionPrefix + "_docs = math.floor((dfill*dB-dmeta) / " + collectionPrefix + "_size)");

			writer.println(1, "#data leaf and internal blocks");
			writer.println(1,
					collectionPrefix + "_leaf  = " + collectionPrefix + "_count / " + collectionPrefix + "_docs");
			writer.println(1, collectionPrefix + "_internal  = " + collectionPrefix
					+ "_leaf * dinternal / ( dfillint * dBint - dmeta)");
//				
			writer.println();
			writer.println(1, "# Collection " + collectionPrefix + " Indexes #####");
			for (DataIndexMetadata meta : col) {
				if (!meta.isData()) {
					String indexPrefix = collectionPrefix + "_index_" + indexCounter;
					writer.println(1, "# Index " + indexPrefix);
					writer.println();
					writer.println(1, indexPrefix + "_mult = " + meta.getSizeOrMult());
					writer.println(1, indexPrefix + "_distinct = " + meta.getDistinctCount());
					writer.println(1, indexPrefix + "_rep = " + indexPrefix + "_mult * " + collectionPrefix
							+ "_count / " + indexPrefix + "_distinct");
					writer.println(1, indexPrefix + "_freq = " + meta.getFrequency());
					indexCounter++;
					writer.println();
					writer.println(1, "# Index " + indexPrefix + " leaf and internal blocks");

					switch (meta.getDataType()) {
					case UUID:
						writer.println(1, indexPrefix + "_leaf = " + collectionPrefix + "_count / primary_idx");
						writer.println(1, indexPrefix + "_internal = " + indexPrefix
								+ "_leaf * iinternal / (ifillint * iBint - imeta)");
						primaryIndexes.add(indexPrefix);
						break;
					case INT:
						writer.println(1, indexPrefix + "_leaf = " + indexPrefix + "_mult * " + collectionPrefix
								+ "_count / secondary_idx");
						writer.println(1, indexPrefix + "_internal = " + indexPrefix
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
			ArrayList<String> secondaryIndexes, TabbedPrintStream writer) {
		writer.println(1, "# saturation memory variables");

		writer.println(1, "T = m.Var(value = 5000)");
//			Pd1 = m.Var(value=1)

		for (String string : collections) {
			writer.println(1, string + "_sat = m.Var(value = 1000)");
			writer.println(1, string + "_prob = m.Var(value = 1)");

		}
		for (String string : primaryIndexes) {
			writer.println(1, string + "_sat = m.Var(value = 1000)");
			writer.println(1, string + "_req = m.Var(value = 1)");
			writer.println(1, string + "_SF = m.Var(value = 1)");
			writer.println(1, string + "_exp = m.Var(value = 1)");
			writer.println(1, string + "_prob = m.Var(value = 1)");
		}
		for (String string : secondaryIndexes) {
			writer.println(1, string + "_sat = m.Var(value = 1000)");
			writer.println(1, string + "_req = m.Var(value = 1)");
			writer.println(1, string + "_SF = m.Var(value = 1)");
			writer.println(1, string + "_exp = m.Var(value = 1)");
			writer.println(1, string + "_prob = m.Var(value = 1)");
		}
	}

	private static void writeProbabilityEqs(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, TabbedPrintStream writer) {
		StringBuilder sb;
		for (String string : collections) {
			writer.println(1, string + "_prob == ");
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
			writer.print(1, sb.toString());
		}

		primaryIndexes.stream().forEach(i -> {
			writer.println(1, i + "_prob == 1 - ( 1 - " + i + "_SF) ** primary_idx ,");
		});

		secondaryIndexes.stream().forEach(i -> {
			writer.println(1, i + "_prob == 1 - ( 1 - " + i + "_SF) ** (secondary_idx /" + i + "_rep) ,");
		});
	}

	private static void writeMemorySum(ArrayList<String> collections, ArrayList<String> primaryIndexes,
			ArrayList<String> secondaryIndexes, TabbedPrintStream writer, boolean eviction) {
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
		writer.print(1, sb.toString());
		writer.println();
	}

	private static void writeFixedContent(TabbedPrintStream writer) {
		writer.println("### Fixed data ######");

		writer.println("from gekko import GEKKO");
		writer.println("import math");
		writer.println("def solve_me():");
		writer.println(1, "m = GEKKO(remote=False)  # create GEKKO model");
		writer.println(1, "#data");
		writer.println(1, "dinternal=22");
		writer.println(1, "idsize=12");
		writer.println(1, "dmeta=6");
		writer.println(1, "dB = 4096*8");
		writer.println(1, "dfill=1");
		writer.println(1, "dBint = 4096");
		writer.println(1, "dfillint=.75");
		writer.println(1, "\n\n");
		writer.println(1, "#index");
		writer.println(1, "iinternal=22");
		writer.println(1, "imeta=30");
		writer.println(1, "iB = 4096*8");
		writer.println(1, "ifill=1");
		writer.println(1, "iBint = 4096*4");
		writer.println(1, "ifillint=.75");
//		writer.println(1,"i=15");
		writer.println(1, "primary_idx = math.floor((ifill*iB-imeta) / 15)");
		writer.println(1, "secondary_idx = math.floor((ifill*iB-imeta) / 8)");
	}

}
