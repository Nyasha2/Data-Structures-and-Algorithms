package edu.caltech.cs2.lab03;

public class OutcomeNode implements DecisionTreeNode {
    public final String outcome;

    public OutcomeNode(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
