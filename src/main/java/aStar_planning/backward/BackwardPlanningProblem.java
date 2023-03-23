package aStar_planning.backward;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.Constraint;
import logic.Goal;
import logic.LogicalInstance;
import logic.Rule;
import logic.Situation;
import outputs.PlanningOutput;
import outputs.TotalOrderPlan;
import planning.Problem;

import java.util.List;

public class BackwardPlanningProblem extends Problem implements AStarProblem {
    private List<Rule> rules;

    public BackwardPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                                   Goal goal, List<Rule> rules) {
        super(initialSituation, possibleActions, goal);
        this.rules = rules;
    }

    @Override
    public State getInitialState() {
        return this.getGoal();
    }

    @Override
    public boolean isFinal(State state) {
        return ((Constraint)state).isVerified((Situation)this.getInitialState());
    }

    @Override
    public List<Operator> getOptions(State state) {
        return ((Constraint)state).getContributingInstances(this.getPossibleActions());
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Constraint)state).revertAction((LogicalInstance)operator);
    }

    @Override
    public double evaluateState(State state) {
        return ((Constraint)state).evaluateCompletion(this.getInitialSituation());
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 1;
    }

    @Override
    public boolean isValid(State state) {
        return ((Constraint)state).isCoherent(this.rules);
    }

    @Override
    public List<Operator> getSolution(List<Operator> solutionSteps) {
        return solutionSteps;
    }

    @Override
    public PlanningOutput outputPlan(State finalState, List<Operator> solutionOperators) {
        return new TotalOrderPlan(solutionOperators);
    }
}
