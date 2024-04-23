package aStar_planning.pop;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.utils.PlanInitializer;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import outputs.PartialOrderPlan;
import outputs.PlanningOutput;
import planning.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A planning problem according to the Partial-Order Planning specification,
 * where a state is
 * represented by a plan and the operators are plan modifications to resolve
 * flaws within the plan.
 * When there is a plan without any flaws, the planning problem is solved. And a
 * total order
 * sequence of actions can be drawn out of the resulting plan.
 */
@NoArgsConstructor
@Getter
@ToString
public class PopPlanningProblem extends Problem implements AStarProblem {
  private Plan initialPlan;
  private final static Logger logger = LogManager.getLogger(PopPlanningProblem.class);

  public PopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
      Goal goal) {
    super(initialSituation, possibleActions, goal);
    this.initialPlan = PlanInitializer.constructInitialPlan(initialSituation, goal);
  }

  /**
   * A constructor in case you don't want to compute the initial state (plan)
   * right away.
   * 
   * @param initialSituation
   * @param possibleActions
   * @param goal
   * @param initialPlanIsNull
   */
  public PopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
      Goal goal, boolean initialPlanIsNull) {
    super(initialSituation, possibleActions, goal);
    this.initialPlan = initialPlanIsNull ? null : new Plan();
  }

  @Override
  public State getInitialState() {
    return this.initialPlan;
  }

  @Override
  public boolean isFinal(State state) {
    return ((Plan) state).isExecutable();
  }

  @Override
  public List<Operator> getOptions(State state) {
    Plan plan = (Plan) state;

    List<Action> allPossibleActions = new ArrayList<>(this.getPossibleActions());

    logOptions(plan, allPossibleActions); // simple log for the console
    return plan.allPossibleModifications(allPossibleActions);
  }

  @Override
  public State apply(Operator operator, State state) {
     logger.info("___APPLYING___ "+operator);
     logger.info("\n___GOT___ "+((Plan)state).applyPlanModification(operator));
    return ((Plan) state).applyPlanModification(operator);
  }

  @Override
  public double evaluateState(State state) {
     logger.info("HEURISTIC : "+((Plan)state).getFlaws().size());
    return ((Plan) state).getFlaws().size();
  }

  @Override
  public double evaluateOperator(Operator transition) {
    return 0.1;
  }

  @Override
  public boolean isValid(State state) {
    logger.info("IS VALID : " + ((Plan)state).isCoherent());
    return ((Plan) state).isCoherent();
  }

  @Override
  public List<Operator> getSolution(List<Operator> solutionSteps) {
    return solutionSteps;
  }

  @Override
  public PlanningOutput outputPlan(State finalState, List<Operator> solutionOperators) {
    Plan solutionPlan = (Plan) finalState;
    solutionPlan.removeRedundantTemporalConstraints();
    PlanningOutput output = new PartialOrderPlan(solutionPlan);

    logger.info("=".repeat(100));
    logger.info("\tFINAL OUTPUT");
    logger.info("=".repeat(100));
    logger.info(output);

    return output;
  }

  public void logOptions(State state, List<Action> possibleActions) {
    Plan plan = (Plan) state;

     AtomicInteger i = new AtomicInteger(0);
     logger.debug("_________GET OPTIONS_________");
     logger.info("CURRENT STATE : "+ plan);
     logger.info("\n______OPTIONS ARE______");
     plan.allPossibleModifications(possibleActions).forEach(operator -> {
     logger.info("--> OPTION #"+(i.incrementAndGet())+" : " +operator);
     });
  }
}
