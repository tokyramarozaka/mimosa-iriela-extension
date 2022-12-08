package aStar_planning.pop;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.utils.PlanInitializer;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import planning.Problem;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class PopPlanningProblem extends Problem implements AStarProblem{
    private Plan initialPlan;

    public PopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                              Goal goal){
        super(initialSituation, possibleActions, goal);
        this.initialPlan = PlanInitializer.constructInitialPlan(initialSituation, goal);
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
        return ((Plan)state).allPossibleModifications(this.getPossibleActions());
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Plan)state).applyPlanModification((PlanModification)operator);
    }

    @Override
    public double evaluateState(State state) {
        return ((Plan)state).getFlaws().size();
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 0;
    }

    @Override
    public boolean isValid(State state) {
        return ((Plan)state).isCoherent();
    }

    /**
     * TODO : Extract the set of action instances from the partial-order planning process
     * @param solutionSteps : the set of actions to carry out to resolve the planning problem.
     * @return
     */
    @Override
    public List<Operator> getSolution(List<Operator> solutionSteps) {
        Collections.reverse(solutionSteps);
        return solutionSteps;
    }

    @Override
    public String showSolution(List<Operator> solutionSteps) {
        Plan plan = this.getInitialPlan();

        solutionSteps.forEach(
            operator -> this.getInitialPlan().applyPlanModification((PlanModification) operator)
        );

        return plan.toString();
    }

}
