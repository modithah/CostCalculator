package edu.upc.essi.catalog.core.constructs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGRandomAccessResult.GotoResult;
import org.hypergraphdb.annotation.HGIgnore;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.query.SubgraphMemberCondition;
import org.hypergraphdb.query.TargetCondition;
import org.hypergraphdb.storage.BAtoHandle;
import org.hypergraphdb.util.FilteredSortedSet;
import org.hypergraphdb.util.Mapping;

/**
 *
 * <p>
 * A {@link HyperNode} that encapsulates a set of atoms from the global
 * {@link HyperGraph} database. A subgraph can be thought of as a hyper-edge in
 * the standard set-theoretic formulation of hypergraphs.
 * </p>
 *
 * @author Borislav Iordanov
 *
 */
public class HGSimpleSubgraph implements HyperNode, HGHandleHolder, HGGraphHolder, Element {

	protected ArrayList<HGHandle> outgoingSet = new ArrayList<>();
	protected ArrayList<HGHandle> incoming = new ArrayList<>();
	protected String name;

	@HGIgnore
	protected HyperGraph graph;
	protected HGHandle thisHandle;
	Mapping<HGHandle, Boolean> memberPredicate = new Mapping<HGHandle, Boolean>() {
		public Boolean eval(HGHandle h) {
			return isMember(h);
		}
	};

	private HGQueryCondition localizeCondition(HGQueryCondition condition) {
		return hg.and(new SubSetCondition (thisHandle), condition);
	}

	private HGQueryCondition localizeCondition() {
		return new SubSetCondition(thisHandle);
	}

	private void unindex(HGHandle h) {
		outgoingSet.remove(h);
		Element atm = graph.get(h);
		atm.removeFromIncoming(thisHandle);
		graph.update(atm);
//		graph.update(this);
	}

	public boolean isMember(HGHandle atom) {
		// it's quicker to lookup the reverse index because we expect fewer subgraphs
		// than
		// atoms in them
//		HGRandomAccessResult<HGPersistentHandle> rs = getReverseIndex().find(atom.getPersistent());
//		try {
//			return rs.goTo(thisHandle.getPersistent(), true) == GotoResult.found;
//		} finally {
//			rs.close();
//		}
		return outgoingSet.contains(atom.getPersistent());
	}

	/**
	 * <p>
	 * Add an existing atom to this {@link HyperNode}. The atom may be a member
	 * multiple nodes at a time.
	 * </p>
	 *
	 * @param atom
	 * @return The <code>atom</code> parameter.
	 */
	public HGHandle add(final HGHandle atom) {
//		return graph.getTransactionManager().ensureTransaction(new Callable<HGHandle>() {
//			public HGHandle call() {
//		index(atom);
//		logger.info("Adding" +atom);
		this.outgoingSet.add(atom);
		Element atm = graph.get(atom);
		atm.addToIncoming(thisHandle);
		graph.update(atm);
//		graph.update(this);
		return atom;
//			}
//		});
	}

	/**
	 * Add to global graph and mark as member of this subgraph.
	 */
	public HGHandle add(Object atom, HGHandle type, int flags) {
		return add(graph.add(atom, type, flags));
	}

	public long count(HGQueryCondition condition) {
		return hg.count(graph, localizeCondition(condition));
	}

	/**
	 * Define in global graph and mark as member of this subgraph.
	 */
	public void define(HGHandle handle, HGHandle type, Object instance, int flags) {
		graph.define(handle, type, instance, flags);
		add(handle);
	}

	public <T> HGSearchResult<T> find(HGQueryCondition condition) {
		return graph.find(localizeCondition(condition));
	}

	@SuppressWarnings("unchecked")
	public <T> T findOne(HGQueryCondition condition) {
		return (T) graph.findOne(localizeCondition(condition));
	}

	@SuppressWarnings("unchecked")
	public <T> T getOne(HGQueryCondition condition) {
		return (T) graph.getOne(localizeCondition(condition));
	}


	public List<HGHandle> findAll(HGQueryCondition condition) {
		return graph.findAll(localizeCondition(condition));
	}

	public List<HGHandle> findAll() {
		return this.outgoingSet;
	}

	public <T> List<T> getAll(HGQueryCondition condition) {
		return graph.getAll(localizeCondition(condition));
	}

	@SuppressWarnings("unchecked")
	public <T> T get(HGHandle handle) {
		return isMember(handle) ? (T) graph.get(handle) : null;
	}

	/**
	 * Return incidence set where each element is a member of this
	 * <code>HGSubgraph</code>. The atom itself whose incidence set is returned
	 * doesn't have to be a member of the subgraph!
	 */
	public IncidenceSet getIncidenceSet(HGHandle handle) {
		// maybe should return empty set, instead of null?
		// but if the atom is not here, one shouldn't be asking for incidence set
		// so null seems more appropriate
		return new IncidenceSet(handle,
				new FilteredSortedSet<HGHandle>(graph.getIncidenceSet(handle), memberPredicate));
	}

	public IncidenceSet getIncidenceSet() {
		// maybe should return empty set, instead of null?
		// but if the atom is not here, one shouldn't be asking for incidence set
		// so null seems more appropriate
		return new IncidenceSet(thisHandle,
				new FilteredSortedSet<HGHandle>(graph.getIncidenceSet(thisHandle), memberPredicate));
	}

	public HGHandle getType(HGHandle handle) {
		return isMember(handle) ? graph.getType(handle) : null;
	}

	/**
	 * Removes the atom globally from the database as well as from the nested graph.
	 *
	 * @param handle            The atom to remove.
	 * @param keepIncidentLinks - whether to also remove links pointing to the
	 *                          removed atom. This parameter applies recursively to
	 *                          the links removed.
	 * @return The result of {remove}.
	 */
	public boolean removeGlobally(HGHandle handle, boolean keepIncidentLinks) {
		unindex(handle);
		return graph.remove(handle, keepIncidentLinks);
	}

	/**
	 * Removes an atom from this scope. The atom is not deleted from the global
	 * {@link HyperGraph} database. If you wish to delete it globally, use
	 * {remove}.
	 *
	 * @return Return value is unreliable
	 */
	public boolean remove(final HGHandle handle) {
		outgoingSet.remove(handle);
		Element atm = graph.get(handle);
		atm.removeFromIncoming(thisHandle);
		graph.update(atm);
		return true;
	}

	/**
	 * Performs the replace in the global database as this only deals with an atom's
	 * value.
	 */
	public boolean replace(final HGHandle handle, final Object newValue, final HGHandle newType) {
		return graph.getTransactionManager().ensureTransaction(new Callable<Boolean>() {
			public Boolean call() {
				return graph.replace(handle, newValue, newType);
			}
		});
	}

	public HGHandle getAtomHandle() {
		return this.thisHandle;
	}

	public void setAtomHandle(HGHandle handle) {
		this.thisHandle = handle;
	}

	public void setHyperGraph(HyperGraph graph) {
		this.graph = graph;
	}

	public HyperGraph getHyperGraph() {
		return this.graph;
	}

	public Iterator<HGHandle> iterator() {
		return findAll().iterator();
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("PlainLink([");
		result.append(outgoingSet.size());
		result.append(']');
		result.append(',');
		result.append(toStringHdlArr(outgoingSet));
		result.append(")");
		return result.toString();
	}

	protected String toStringHdlArr(ArrayList<HGHandle> hdls) {
		StringBuffer result = new StringBuffer();
		for (HGHandle hgHandle : hdls) {
			result.append(',');

			result.append(hgHandle);
		}

		return result.toString();
	}

	public void setNewOutgoing(ArrayList<HGHandle> newobjs) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public ArrayList<HGHandle> getParents() {
		return incoming;
	}

	public ArrayList<HGHandle> getIncoming() {
		return incoming;
	}

	public void setIncoming(ArrayList<HGHandle> incoming) {
		this.incoming = incoming;
	}

	@Override
	public void addToIncoming(HGHandle parent) {
		this.incoming.add(0,parent);
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<HGHandle> getOutgoingSet() {
		return outgoingSet;
	}

	public void setOutgoingSet(ArrayList<HGHandle> outgoingSet) {
		this.outgoingSet = outgoingSet;
	}

	@Override
	public void removeFromIncoming(HGHandle parent) {
		this.incoming.remove(parent.getPersistent());
	}

//	public static SubSetCondition included(HGHandle parent,HGHandle atom) {
//		return new SubSetCondition(parent, atom);
//	}
}