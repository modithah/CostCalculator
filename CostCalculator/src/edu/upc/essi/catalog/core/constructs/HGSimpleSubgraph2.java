//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.upc.essi.catalog.core.constructs;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import org.hypergraphdb.HGGraphHolder;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGHandleHolder;
import org.hypergraphdb.HGIndex;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGRandomAccessResult;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HyperNode;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGRandomAccessResult.GotoResult;
import org.hypergraphdb.annotation.HGIgnore;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.query.SubgraphMemberCondition;
import org.hypergraphdb.storage.BAtoHandle;
import org.hypergraphdb.util.FilteredSortedSet;
import org.hypergraphdb.util.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HGSimpleSubgraph2 implements HyperNode, HGHandleHolder, HGGraphHolder {
	private static final String IDX_NAME = "subgraph.index";
	private static final String REVIDX_NAME = "revsubgraph.index";
	@HGIgnore
	protected HyperGraph graph;
	protected HGHandle thisHandle;
	protected String name;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	Mapping<HGHandle, Boolean> memberPredicate = new Mapping<HGHandle, Boolean>() {
		public Boolean eval(HGHandle h) {
			return HGSimpleSubgraph2.this.isMember(h);
		}
	};

	public HGSimpleSubgraph2() {
	}

	private HGQueryCondition localizeCondition(HGQueryCondition condition) {
		return hg.and(new HGQueryCondition[]{new SubgraphMemberCondition(this.thisHandle), condition});
	}

	private HGQueryCondition localizeCondition() {
		return new SubgraphMemberCondition(this.thisHandle);
	}

	private HGIndex<HGPersistentHandle, HGPersistentHandle> getIndex() {
		return this.graph.getStore().getIndex("subgraph.index", BAtoHandle.getInstance(this.graph.getHandleFactory()), BAtoHandle.getInstance(this.graph.getHandleFactory()), (Comparator)null, true);
	}

	private HGIndex<HGPersistentHandle, HGPersistentHandle> getReverseIndex() {
		return this.graph.getStore().getIndex("revsubgraph.index", BAtoHandle.getInstance(this.graph.getHandleFactory()), BAtoHandle.getInstance(this.graph.getHandleFactory()), (Comparator)null, true);
	}

//	public static HGIndex<HGPersistentHandle, HGPersistentHandle> getReverseIndex(HyperGraph atGraph) {
//		return atGraph.getStore().getIndex("revsubgraph.index", BAtoHandle.getInstance(atGraph.getHandleFactory()), BAtoHandle.getInstance(atGraph.getHandleFactory()), (Comparator)null, true);
//	}
//
//	public static HGIndex<HGPersistentHandle, HGPersistentHandle> getIndex(HyperGraph atGraph) {
//		return atGraph.getStore().getIndex("subgraph.index", BAtoHandle.getInstance(atGraph.getHandleFactory()), BAtoHandle.getInstance(atGraph.getHandleFactory()), (Comparator)null, true);
//	}

	private void index(HGHandle h) {
		logger.info("BEFORE");
		logger.info(this.getIndex().find(this.thisHandle.getPersistent()).toString());
		logger.info(this.getReverseIndex().find(h.getPersistent()).toString());
		this.getIndex().addEntry(this.thisHandle.getPersistent(), h.getPersistent());
		this.getReverseIndex().addEntry(h.getPersistent(), this.thisHandle.getPersistent());
		logger.info("AFTER");
		logger.info(this.getIndex().find(this.thisHandle.getPersistent()).toString());
		logger.info(this.getReverseIndex().find(h.getPersistent()).toString());
	}

	private void unindex(HGHandle h) {

		this.getIndex().removeAllEntries(this.thisHandle.getPersistent());
//
//		this.getReverseIndex().removeEntry(h.getPersistent(), this.thisHandle.getPersistent());
		this.getReverseIndex().removeAllEntries(h.getPersistent());
		this.getReverseIndex().removeAllEntries(this.thisHandle.getPersistent());
		this.getIndex().removeAllEntries(h.getPersistent());
		logger.info("FUCKING SHIT");
	}

	public boolean isMember(HGHandle atom) {
		HGRandomAccessResult rs = this.getReverseIndex().find(atom.getPersistent());

		boolean var3;
		try {
			var3 = rs.goTo(this.thisHandle.getPersistent(), true) == GotoResult.found;
		} finally {
			rs.close();
		}

		return var3;
	}

	public HGHandle add(final HGHandle atom) {
		return (HGHandle)this.graph.getTransactionManager().ensureTransaction(new Callable<HGHandle>() {
			public HGHandle call() {
				HGSimpleSubgraph2.this.index(atom);
				return atom;
			}
		});
	}

	public HGHandle add(Object atom, HGHandle type, int flags) {
		return this.add(this.graph.add(atom, type, flags));
	}

	public long count(HGQueryCondition condition) {
		return hg.count(this.graph, this.localizeCondition(condition));
	}

	public void define(HGHandle handle, HGHandle type, Object instance, int flags) {
		this.graph.define(handle, type, instance, flags);
		this.add(handle);
	}

	public <T> HGSearchResult<T> find(HGQueryCondition condition) {
		return this.graph.find(this.localizeCondition(condition));
	}

	public <T> T findOne(HGQueryCondition condition) {
		return this.graph.findOne(this.localizeCondition(condition));
	}

	public <T> T getOne(HGQueryCondition condition) {
		return this.graph.getOne(this.localizeCondition(condition));
	}

	public List<HGHandle> findAll(HGQueryCondition condition) {
		return this.graph.findAll(this.localizeCondition(condition));
	}

	public List<HGHandle> findAll() {
		return this.graph.findAll(this.localizeCondition());
	}

	public <T> List<T> getAll(HGQueryCondition condition) {
		return this.graph.getAll(this.localizeCondition(condition));
	}

	public <T> List<T> getAll() {
		return this.graph.getAll(this.localizeCondition());
	}


	public <T> T get(HGHandle handle) {
		return this.isMember(handle) ? this.graph.get(handle) : null;
	}

	public IncidenceSet getIncidenceSet(HGHandle handle) {
		return new IncidenceSet(handle, new FilteredSortedSet(this.graph.getIncidenceSet(handle), this.memberPredicate));
	}

	public IncidenceSet getIncidenceSet() {
		return new IncidenceSet(thisHandle, new FilteredSortedSet(this.graph.getIncidenceSet(thisHandle), this.memberPredicate));
	}

	public HGHandle getType(HGHandle handle) {
		return this.isMember(handle) ? this.graph.getType(handle) : null;
	}

	public boolean removeGlobally(HGHandle handle) {
		this.unindex(handle);
		return this.graph.remove(handle);
	}

	public boolean removeGlobally(HGHandle handle, boolean keepIncidentLinks) {
		this.unindex(handle);
		return this.graph.remove(handle, keepIncidentLinks);
	}

	public boolean remove(final HGHandle handle) {
		return (Boolean)this.graph.getTransactionManager().ensureTransaction(new Callable<Boolean>() {
			public Boolean call() {
				boolean ret = HGSimpleSubgraph2.this.isMember(handle);
				logger.info("-------" + ret);
				HGSimpleSubgraph2.this.unindex(handle);
				return ret;
			}
		});
	}

	public boolean replace(final HGHandle handle, final Object newValue, final HGHandle newType) {
		return (Boolean)this.graph.getTransactionManager().ensureTransaction(new Callable<Boolean>() {
			public Boolean call() {
				return HGSimpleSubgraph2.this.graph.replace(handle, newValue, newType);
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

	public void remove() {

		this.graph.getTransactionManager().ensureTransaction(new Callable<Boolean>() {
			public Boolean call() {
				boolean ret = true;
				HGSimpleSubgraph2.this.removeAll();
				return ret;
			}
		});



	}

	public void removeAll(){
		this.getIndex().removeAllEntries(this.thisHandle.getPersistent());
//		this.getReverseIndex().removeAllEntries(h.getPersistent());
		this.getReverseIndex().removeAllEntries(this.thisHandle.getPersistent());
//		this.getIndex().removeAllEntries(h.getPersistent());
		logger.info("FUCKING SHIT");

	}

	public void setName(String name) {
		this.name = name;
	}


}
