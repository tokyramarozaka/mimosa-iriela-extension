package aStar_planning.pop;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.Context;
import logic.ContextualPredicate;
import logic.Goal;
import logic.LogicalInstance;
import logic.Situation;

import java.util.ArrayList;
import java.util.List;

/**
 * A tool to start Partial-Order Planning with some initial plan, namely :
 * <ul>
 *     <li>An initial situation</li>
 *     <li>A final situation where the goals are met</li>
 *     <li>Two(2) dummy steps, one before the initial situation, one after the final situation</li>
 * </ul>
 */
public class PlanInitializer {
    /**
     * Returns the initial plan, with the needed situations, steps, and constraints
     * @param initialSituation : the initial situation to fetch all the propositions that are
     *                         necessarily true in the starting dummy situation
     * @param goal: the goal which are converted as preconditions of the final step of the plan
     * @return an initial plan to begin working with Partial-Order Planning
     */
    public static Plan constructInitialPlan(Situation initialSituation, Goal goal) {
        List<PopSituation> initialAndFinalSituations = buildDummySituations();
        List<Step> initialAndFinalSteps = buildDummySteps(initialSituation, goal);

        CodenotationConstraints initialCodenotationConstraints = new CodenotationConstraints();
        TemporalConstraints initialTemporalConstraints = buildInitialTemporalConstraints(
                initialAndFinalSituations,
                initialAndFinalSteps
        );

        return new Plan(initialAndFinalSituations, initialAndFinalSteps,
                initialCodenotationConstraints, initialTemporalConstraints);
    }

    private static List<PopSituation> buildDummySituations(){
        PopSituation initialSituation = new PopSituation(), finalSituation = new PopSituation();

        List<PopSituation> dummySituations = new ArrayList<>();
        dummySituations.add(initialSituation);
        dummySituations.add(finalSituation);

        return dummySituations;
    }

    /**
     * Returns both the initial step which asserts the initial situation, and the last step which
     * requires the goal as preconditions
     * @param initialSituation : the initial situation to fetch the initial step's consequences
     * @param goal : the goal to build the preconditions of the final dummy step
     * @return a List of steps, where <b>the element at 0 is the initial step</b> and <b> the
     * element at 1 is the last step</b>
     */
    private static List<Step> buildDummySteps(Situation initialSituation, Goal goal){
        List<Step> dummySteps = new ArrayList<>();

        LogicalInstance initialStep = new LogicalInstance(
                initialAction(initialSituation.getContextualPredicates()),
                new Context()
        );

        LogicalInstance finalStep = new LogicalInstance(
                finalAction(goal.getGoalPropositions()),
                new Context()
        );

        return dummySteps;
    }

    /**
     * Builds the initial Action to assert the propositions of the initial situation
     * @param propositions : the propositions to use as consequences of the step
     * @return an action whose consequences assert the initial situation
     */
    private static Action initialAction(List<ContextualPredicate> propositions){
        return new Action(
                "initial",
                new ActionPrecondition(),
                new ActionConsequence(propositions
                        .stream()
                        .map(proposition -> new Atom(false, proposition.getPredicate()))
                        .toList()
                )
        );
    }

    /**
     * Builds the final action of the plan which requires the goal as preconditions
     * @param propositions : the propositions of the goal to serve as preconditions
     * @return a dummy Action the goal to be met in the final situation of the plan
     */
    private static Action finalAction(List<Atom> propositions){
        return new Action(
                "final",
                new ActionPrecondition(propositions.stream().toList()),
                new ActionConsequence()
        );
    }

    /**
     * Describes the partial-order of the elements in the initial plan, namely :
     * <ul>
     *     <li>the first step is before initial situation</li>
     *     <li>the initial situation is before the final situation</li>
     *     <li>the final situation is before the final step</li>
     * </ul>
     * @param situations : the situations to be arranged
     * @param steps : the steps to be arranged
     * @return a Temporal constraint on the given situations and steps for POP
     */
    private static TemporalConstraints buildInitialTemporalConstraints(List<PopSituation> situations,
                                                                      List<Step> steps) {
        List<PartialOrder> partialOrders = new ArrayList<>();

        PopSituation initialSituation = situations.get(0);
        PopSituation finalSituation = situations.get(1);

        Step initialStep = steps.get(0);
        Step finalStep = steps.get(1);

        partialOrders.add(new PartialOrder(initialStep, initialSituation));
        partialOrders.add(new PartialOrder(initialSituation, finalSituation));
        partialOrders.add(new PartialOrder(finalSituation, finalStep));

        return new TemporalConstraints(partialOrders);
    }
}
