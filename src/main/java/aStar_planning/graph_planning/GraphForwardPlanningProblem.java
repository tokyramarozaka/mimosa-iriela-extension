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
        return ((Node)state).getLinks().stream().collect(Collectors.toList());
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Link)operator).getTo();
    }

    /**
     * TODO : find a heuristic for graph-based search
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
