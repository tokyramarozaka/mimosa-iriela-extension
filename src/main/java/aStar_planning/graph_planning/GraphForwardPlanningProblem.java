package aStar_planning.graph_planning;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import graph.Link;
import graph.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import outputs.PlanningOutput;
import outputs.TotalOrderPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class GraphForwardPlanningProblem implements AStarProblem {
    private Node initialNode;
    private Node goalNode;

    @Override
    public State getInitialState() {
        return this.initialNode;
    }

    @Override
    public boolean isFinal(State state) {
        return state.equals(goalNode);
    }

    @Override
    public List<Operator> getOptions(State state) {
        return new ArrayList<>(((Node) state).getLinks());
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Link)operator).getTo();
    }

    /**
     * Return the distance of a given node from the goal node. Since in a graph it is impossible to
     * know the goal's distance, we simply leave it as 0. Leaving evaluateOperator i.e. the plan's
     * length as the only measure to see if a plan is better than another.
     * @param state : the state to be evaluated
     * @return a double representing the distance from the goal
     */
    @Override
    public double evaluateState(State state) {
        return 0;
    }

    @Override
    public double evaluateOperator(Operator operator) {
        return 1;
    }

    @Override
    public boolean isValid(State state) {
        return true;
    }

    @Override
    public List<Operator> getSolution(List<Operator> solutionOperators) {
        return solutionOperators;
    }

    @Override
    public PlanningOutput outputPlan(State finalState, List<Operator> solutionOperators) {
        return new TotalOrderPlan(solutionOperators);
    }
}
