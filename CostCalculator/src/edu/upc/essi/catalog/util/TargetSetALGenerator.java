package edu.upc.essi.catalog.util;

import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.algorithms.HGALGenerator;
import org.hypergraphdb.util.Pair;

import edu.upc.essi.catalog.core.constructs.Hyperedge;

/**
 * <p>
 * The <code>TargetSetALGenerator</code> produces all atoms targeted by the given link.
 * If called with a non link, it will not throw an exception.
 * </p>
 *  
 * @author Thomas Hilpold, Borislav Iordanov
 */
public class TargetSetALGenerator implements HGALGenerator 
{
	private HyperGraph graph;
	
	private class TargetIterator implements HGSearchResult<Pair<HGHandle,HGHandle>>
	{
		HGHandle srcHandle;
		Hyperedge srcAsLink;
		int srcArity;
		int curTargetIndex;
		Pair<HGHandle,HGHandle> current;
				
		TargetIterator(HGHandle src)
		{
			this.srcHandle = src;
			Object srcObject = graph.get(src);
//			logger.info(srcObject.getClass().getSuperclass().getSuperclass();
			if (srcObject instanceof Hyperedge) {
				this.srcAsLink = (Hyperedge) srcObject;
				this.srcArity = srcAsLink.getArity();
//				logger.info(this.srcArity);
			} else {
				//don't fail, let hasPrev/hasNext be false.
				this.srcAsLink = null;
				this.srcArity = 0;
			}
			//this.closeResultSet = closeResultSet;
			curTargetIndex = -1;
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
		
		public boolean hasNext()
		{
			return curTargetIndex < srcArity - 1;
		}
		
		public Pair<HGHandle,HGHandle> next()
		{
			curTargetIndex ++;
			current = new Pair<HGHandle,HGHandle>(srcHandle, srcAsLink.getTargetAt(curTargetIndex));	        
			return current;
		}

		public void close()
		{
			//do nothing
		}

		public Pair<HGHandle,HGHandle> current()
		{
			return current;
		}

		public boolean isOrdered()
		{
			return true;
		}

		public boolean hasPrev() { 
			return curTargetIndex >= 0;
		}
		
		public Pair<HGHandle,HGHandle> prev() { 
			current = new Pair<HGHandle,HGHandle>(srcHandle, srcAsLink.getTargetAt(curTargetIndex));	        
			curTargetIndex --;
			return current;	
		}				
	}

	/**
	 * <p>
	 * Empty constructor - you will need to set the graph (see {@link setGraph}) before
	 * the instance becomes usable.
	 * </p>
	 */
	public TargetSetALGenerator()
	{		
	}
	
	/**
	 * <p>Construct a <code>TargetSetALGenerator</code> for the given HyperGraph instance.</p>
	 * 
	 * @param hg The HyperGraph instance.
	 */
	public TargetSetALGenerator(HyperGraph hg)
	{
		this.graph = hg;
	}
	
	public HGSearchResult<Pair<HGHandle,HGHandle>> generate(HGHandle h) 
	{
		return new TargetIterator(h);
	}
	
	
	public void setGraph(HyperGraph graph)
	{
		this.graph = graph;
		
	}
	
	public HyperGraph getGraph()
	{
		return this.graph;
	}
}