package edu.upc.essi.catalog.core.constructs;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery;
import org.hypergraphdb.HyperGraph;


import edu.upc.essi.catalog.enums.HyperedgeTypeEnum;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hyperedge extends HGSimpleSubgraph  {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	private ArrayList<HGHandle> outgoingSet = new ArrayList<>();

	public Hyperedge() {
	}

	private String id;
	private HyperedgeTypeEnum type;
	private double count = 1;
	private HGHandle root = null;
	private HashMap<HGHandle, Relationship> relMap = new HashMap<>();
	private HashMap<Atom, Double> multipliers=null;
	private double size=0.0;

	public Hyperedge(HyperGraph graph, HGHandle handle, String name, HyperedgeTypeEnum type, HGHandle... targetSet) {
		super();
		this.name = name;
		this.type = type;
		this.size=0.0;
		this.multipliers= new HashMap<>();
		setHyperGraph(graph);
		setAtomHandle(handle);
		relMap = new HashMap<>();
		for (int i = 0; i < targetSet.length; i++) {
			if ((type == HyperedgeTypeEnum.Struct || type == HyperedgeTypeEnum.SecondLevel) && i == 0) {
				this.root = targetSet[i];
			}
			add(targetSet[i]);

		}
//		logger.info("printing");
//		print(0);
	}


	@Override
	public HGHandle add(HGHandle atom) {
		HGHandle y = super.add(atom);
//		logger.info((y.equals(atom))+ "--- " + (y==atom));
//		outgoingSet.add(y);
		return y;
	}

	public Hyperedge(HGHandle... targets) {
		for (int i = 0; i < targets.length; i++) {
//			if (i == 0) {
//				this.root = targets[i];
//			}
			add(targets[i]);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HyperedgeTypeEnum getType() {
		return type;
	}

	public void setType(HyperedgeTypeEnum type) {
		this.type = type;
	}

	public String toString() {
		return name + "  " + type + "[" + count(HGQuery.hg.memberOf(thisHandle)) + "]" + getCount() + relMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hyperedge other = (Hyperedge) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if(type != other.type){
			return false;
		}
		if(incoming.size() != other.incoming.size())
			return false;
		for (int i=0;i<incoming.size();i++)
			if(!incoming.get(i).getPersistent().equals(other.incoming.get(i).getPersistent()))
		if (getArity() != other.getArity())
			return false;
		for (int i = 0; i < getArity(); i++)
			if (!getTargetAt(i).getPersistent().equals(other.getTargetAt(i).getPersistent()))
				return false;
		return true;
	}

	public HashMap<Atom, Double> getMultipliers() {
		return multipliers;
	}

	public void setMultipliers(HashMap<Atom, Double> multipliers) {
//		if(this.multipliers==null) {
//			this.multipliers= new HashMap<>();
//		}
//		for (HGHandle a : multipliers.keySet()) {
//			this.multipliers.put(a, multipliers.get(a));
//		}
//		logger.info("Setting" );
		this.multipliers = multipliers;
	}

	public HGHandle getTargetAt(int i) {
		// TODO Auto-generated method stub
		return outgoingSet.get(i);
	}

	public int getArity() {
		// TODO Auto-generated method stub
		return outgoingSet.size();
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public HGHandle getRoot() {
		return root;
	}

	public void setRoot(HGHandle root) {
		this.root = root;
	}

	public void addToMap(HGHandle key, Relationship rel) {
		this.relMap.put(key, rel);
	}

	public Relationship getNestedRelationship(HGHandle key) {
		return relMap.get(key);
	}

	public HashMap<HGHandle, Relationship> getRelMap() {
		return relMap;
	}

	public void setRelMap(HashMap<HGHandle, Relationship> relMap) {
		this.relMap = relMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public void print(FileWriter myWriter, int tabs) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tabs; i++) {
			sb.append(" ");
		}
		try {
			myWriter.write(sb.toString() + type + " -> " + name + "\n");

			Iterator<HGHandle> seconditer = this.findAll().iterator();

			while (seconditer.hasNext()) {
				HGHandle hgHandle2 = (HGHandle) seconditer.next();

				Object a = graph.get(hgHandle2);
//		logger.info(a);
				if (a instanceof Hyperedge) {
					((Hyperedge) a).print(myWriter, tabs + 4);
				}

				if (a instanceof Atom) {
					myWriter.write(sb.toString() + ((Atom) a).getName() + "\n");
				}
				if (a instanceof Relationship) {
					myWriter.write(sb.toString() + ((Relationship) a) + "\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(int tabs) {

		if(!this.getMultipliers().keySet().isEmpty()) {
			System.out.println("Size ->"+ this.getSize());
			System.out.println(String.valueOf(this.getMultipliers()));
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tabs; i++) {
			sb.append(" ");
		}
		System.out.println(sb.toString() + type + " -> " + name);
		Iterator<HGHandle> seconditer = this.findAll().iterator();

		while (seconditer.hasNext()) {
			HGHandle hgHandle2 = (HGHandle) seconditer.next();

			Object a = graph.get(hgHandle2);
//		logger.info(a);
			if (a instanceof Hyperedge) {
				((Hyperedge) a).print(tabs + 4);
			}

			if (a instanceof Atom) {
				System.out.println(sb.toString() + ((Atom) a).getName());
			}
			if (a instanceof Relationship) {
				System.out.println(sb.toString() + ((Relationship) a));
			}
		}
	}

	public String printToString(int tabs) {
		StringBuilder out = new StringBuilder();
		if(!this.getMultipliers().keySet().isEmpty()) {
			out.append("Size ->"+ this.getSize()+"\n");
			out.append(this.getMultipliers()+"\n");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tabs; i++) {
			sb.append(" ");
		}
		out.append(sb.toString() + type + " -> " + name+"\n");
		Iterator<HGHandle> seconditer = this.findAll().iterator();

		while (seconditer.hasNext()) {
			HGHandle hgHandle2 = (HGHandle) seconditer.next();

			Object a = graph.get(hgHandle2);
//		logger.info(a);
			if (a instanceof Hyperedge) {
				out.append( ((Hyperedge) a).printToString(tabs + 4) +"\n");
			}

			if (a instanceof Atom) {
				out.append(sb.toString() + ((Atom) a).getName() + "\n");
			}
			if (a instanceof Relationship) {
				out.append(sb.toString() + ((Relationship) a) + "\n");
			}
		}
		return out.toString();
	}

	public JSONObject printToJSON() {
		JSONObject obj = new JSONObject();
		try {
//		StringBuilder out = new StringBuilder();
		if(!this.getMultipliers().keySet().isEmpty()) {
//			out.append("Size :"+ this.getSize()+",\n");
//			out.append(this.getMultipliers()+"\n");
			obj.put("collection",this.getName());
		}
		StringBuilder sb = new StringBuilder();


//		out.append( name+"\n");

		Iterator<HGHandle> seconditer = this.findAll().iterator();

		while (seconditer.hasNext()) {
			HGHandle hgHandle2 = (HGHandle) seconditer.next();

			Object a = graph.get(hgHandle2);
//		logger.info(a);
			if (a instanceof Hyperedge) {
				obj.put(((Hyperedge) a).getName(),((Hyperedge) a).printToJSON());
//				out.append( ((Hyperedge) a).printToJSON(tabs + 4) +"\n");
			}

			if (a instanceof Atom) {
					obj.put(((Atom) a).getName(),(((Atom) a).getName().contains("ID")?"int":"varchar"));
				}
//				out.append(sb.toString() + ((Atom) a).getName() +":<"+ (((Atom) a).getName().contains("ID")?"int":"varchar")+">\n");
			}
//			if (a instanceof Relationship) {
//				out.append(sb.toString() + ((Relationship) a) + "\n");
//			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
//		out.append(sb.toString()+"}");
		return obj;
	}

	public Iterator<HGHandle> iterator() {
		return findAll().iterator();
	}
}
