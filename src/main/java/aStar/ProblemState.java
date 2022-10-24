package aStar;
public class ProblemState implements Comparable<ProblemState> {
    private ProblemState parent;
    private State state;
    private Operator appliedOperator;
    double g;
    double h;

    public ProblemState(ProblemState parent, State world, Operator appliedOperator, double g, double h) {
        this.parent = parent;
        this.state = world;
        this.appliedOperator = appliedOperator;
        this.g = g;
        this.h = h;
    }

    public double getF() {
        return g + h;
    }

    public ProblemState getParent() {
        return this.parent;
    }

    public State getState() {
        return this.state;
    }

    public Operator getAppliedOperator() {
        return this.appliedOperator;
    }

    @Override
    public int compareTo(ProblemState o) {
        return Double.compare(this.getF(), o.getF());
    }
}

