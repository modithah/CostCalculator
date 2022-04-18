package edu.upc.essi.catalog.core.constructs;

import org.hypergraphdb.*;
import org.hypergraphdb.query.HGAtomPredicate;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.query.SubgraphContainsCondition;

public class SubSetCondition implements HGQueryCondition, HGAtomPredicate {
    private HGHandle parent;

    public SubSetCondition() {
    }

    public SubSetCondition(HGHandle parent) {
        this.parent=parent;
    }

    @Override
    public boolean satisfies(HyperGraph hyperGraph, HGHandle hgHandle) {

        Hyperedge parentElm = hyperGraph.get(parent);
        Element childElm = hyperGraph.get(hgHandle);

        boolean var5;

        var5 = parentElm.isMember(hgHandle.getPersistent()) && childElm.getParents().contains(parent.getPersistent());


        return var5;
    }

    public int hashCode() {
        return this.parent.hashCode();
    }

    public boolean equals(Object x) {
        if (!(x instanceof SubgraphContainsCondition)) {
            return false;
        } else {
            SubgraphContainsCondition c = (SubgraphContainsCondition)x;
            return this.parent.equals(c.getAtom());
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("SubSetCondition(");
        result.append("parent:");
        result.append(this.parent);
        result.append(")");
        return result.toString();
    }

    public HGHandle getParent() {
        return parent;
    }

    public void setParent(HGHandle parent) {
        this.parent = parent;
    }
}
