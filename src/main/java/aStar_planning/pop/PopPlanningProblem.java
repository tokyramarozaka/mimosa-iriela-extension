package aStar_planning.pop;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import planning.Problem;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class PopPlanningProblem extends Problem implements AStarProblem{
    private Plan initialPlan;

    public PopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                              Goal goal){
        super(initialSituation, possibleActions, goal);
        buildInitialPlan();
    }

    private void buildInitialPlan() {
        PopSituation initialSituation = ;
    }

    @Override
    public State getInitialState() {
        return this.initialPlan;
    }

    @Override
    public boolean isFinal(State state) {
        return ((Plan)state).isExecutable();
    }

    @Override
    public List<Operator> getOptions(State state) {
        return ((Plan)state).possibleModifications();
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Plan)state).applyPlanModification((Modification)operator);
    }

    @Override
    public double evaluateState(State state) {
        return ((Plan)state).getFlaws().size();
    }

    @Override
    public double evaluateTransition(Operator transition) {
        return 0;
    }

    @Override
    public boolean isValid(State state) {
        return ((Plan)state).isCoherent();
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
