package aStar;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.PriorityQueue;

/**
 * A problem state represents a state that needs to be sorted using a heurstic function f
 * It is considered a problem since it needs to be explored, therefore, we need to tell how
 * high is it in the priority queue of all pending states.
 */
@EqualsAndHashCode
@ToString
public class ProblemState implements Comparable<ProblemState> {
    private ProblemState parent;
    private State state;
    private Operator appliedOperator;
    double g;
    double h;

    public ProblemState(
            ProblemState parent,
            State world,
            Operator appliedOperator,
            double g,
            double h
    ){
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

