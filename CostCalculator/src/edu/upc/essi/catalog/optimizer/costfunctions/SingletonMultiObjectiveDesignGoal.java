package edu.upc.essi.catalog.optimizer.costfunctions;

public enum SingletonMultiObjectiveDesignGoal {

    INSTANCE;

    private DesignGoal DG;

    public void init (DesignGoal DG) {
        this.DG = DG;
    }

    public DesignGoal getDG() {
        return this.DG;
    }

    public void destroy() {
        this.DG = null;
    }
}
