package aStar_planning.pop;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.utils.PlanInitializer;
import logic.Action;
import logic.Goal;
import logic.LogicalInstance;
import logic.Situation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planning.Problem;

import java.util.Collections;
import java.util.List;

/**
 * A planning problem according to the Partial-Order Planning specification, where a state is
 * represented by a plan and the operators are plan modifications to resolve flaws within the plan.
 * When there is a plan without any flaws, the planning problem is solved. And a total order
 * sequenc of actions can be drawn out of the resulting plan.
 */
@NoArgsConstructor
@Getter
@ToString
public class PopPlanningProblem extends Problem implements AStarProblem{
    private Plan initialPlan;
    private final static Logger logger = LogManager.getLogger(PopPlanningProblem.class);

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
        logger.info("GET OPTIONS FOR : "+state);
        logger.info("\nOPTIONS ARE : ");
        ((Plan)state).allPossibleModifications(this.getPossibleActions()).forEach(e -> {
            logger.info("\n--> OPTION # 1 "+e);
        });
        return ((Plan)state).allPossibleModifications(this.getPossibleActions());
    }

    @Override
    public State apply(Operator operator, State state) {
        logger.info("APPLYING "+operator);
        logger.info("\nGOT "+((Plan)state).applyPlanModification(operator));
        return ((Plan)state).applyPlanModification(operator);
    }

    @Override
    public double evaluateState(State state) {
        logger.info("HEURISTIC : "+((Plan)state).getFlaws().size());
        return ((Plan)state).getFlaws().size();
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 0;
    }

    @Override
    public boolean isValid(State state) {
        logger.info("IS VALID ? "+((Plan)state).isCoherent());
        return ((Plan)state).isCoherent();
    }

    /**
     * TODO : Extract a set of action instances to execute from the partial-order planning process
     * @param solutionSteps : the set of plan modifications resolving the planning problem.
     * @return
     */
    @Override
    public List<Operator> getSolution(List<Operator> solutionSteps) {
        Collections.reverse(solutionSteps);

        Plan finalPlan = getFinalPlan(solutionSteps);

        return finalPlan.createInstance();
    }

    @Override
    public String showSolution(List<Operator> solutionSteps) {
        Plan plan = this.getInitialPlan();

        solutionSteps.forEach(
            operator -> this.getInitialPlan().applyPlanModification((PlanModification) operator)
        );

        return plan.toString();
    }

    public Plan getFinalPlan(List<Operator> allModifications){
        Plan plan = this.getInitialPlan();

        for (Operator operator : allModifications) {
            plan = (Plan) this.getInitialPlan().applyPlanModification(operator);
        }

        return plan;
    }
}
