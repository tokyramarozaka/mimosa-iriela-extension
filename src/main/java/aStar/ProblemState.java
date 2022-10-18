package aStar;
public class ProblemState implements Comparable<ProblemState> {
    private ProblemState parent;
    private State state;
    private Operator operatorTaken;
    double g,h;

    public ProblemState(ProblemState parent, State world, Operator transitionTaken, double g, double h) {
        this.parent = parent;
        this.state = world;
        this.operatorTaken = transitionTaken;
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

    public Operator getTransitionTaken() {
        return this.operatorTaken;
    }

    @Override
    public int compareTo(ProblemState o) {
        return Double.compare(this.getF(), o.getF());
    }
}

