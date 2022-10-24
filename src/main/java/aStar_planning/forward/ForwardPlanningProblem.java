package aStar_planning.forward;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import logic.LogicalInstance;
import logic.Situation;
import planning.Problem;

import java.util.ArrayList;
import java.util.List;

public class ForwardPlanningProblem extends Problem implements AStarProblem {
    @Override
    public State getInitialState() {
        return this.getInitialSituation();
    }

    @Override
    public boolean isFinal(State state) {
        return ((Situation)state).satisfies(this.getGoal());
    }

    @Override
    public List<Operator> getOptions(State state) {
        Situation currentSituation = ((Situation)state);
        List<Operator> options = new ArrayList<>();

        currentSituation
                .allPossibleActionInstances(this.getPossibleActions())
                .forEach(possibleActionInstance -> options.add(possibleActionInstance));

        return options;
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Situation)state).applyActionInstance((LogicalInstance)operator);
    }

    @Override
    public double evaluateState(State state) {
        return ((Situation)state).goalDistance(this.getGoal());
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 0;
    }

    @Override
    public boolean isValid(State state) {
        return true;
    }

    @Override
    public List<Operator> getSolution(List<Operator> solutionSteps) {
        return null;
    }

    @Override
    public String showSolution(List<Operator> solutionSteps) {
        return null;
    }
}
