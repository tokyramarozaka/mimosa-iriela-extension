package aStar_planning.pop.utils;

import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.OrganizationalPlan;
import constraints.Codenotation;
import constraints.PartialOrder;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import constraints.TemporalConstraints;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import constraints.CodenotationConstraints;
import logic.Context;
import logic.ContextualPredicate;
import logic.Goal;
import logic.LogicalInstance;
import logic.Predicate;
import logic.Situation;
import logic.mappers.GoalMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A tool to start Partial-Order Planning with some initial plan, namely :
 * <ul>
 *     <li>An initial situation</li>
 *     <li>A final situation where the goals are met</li>
 *     <li>Two(2) dummy steps, one before the initial situation, one after the final situation</li>
 * </ul>
 */
public class PlanInitializer {
    private final static Logger logger = LogManager.getLogger(PlanInitializer.class);

    /**
     * Returns the initial plan, with the needed situations, steps, and constraints
     *
     * @param initialSituation : the initial situation to fetch all the propositions that are
     *                         necessarily true in the starting dummy situation
     * @param goal:            the goal which are converted as preconditions of the final step of the plan
     * @return an initial plan to begin working with Partial-Order Planning
     */
    public static Plan constructInitialPlan(Situation initialSituation, Goal goal) {
        List<PopSituation> initialAndFinalSituations = buildDummySituations();
        List<Step> initialAndFinalSteps = buildDummySteps(initialSituation, goal);

        Context goalContext = initialAndFinalSteps.get(1).getActionInstance().getContext();
        CodenotationConstraints initialCodenotationConstraints = buildInitialCc(goal,goalContext);

        TemporalConstraints initialTemporalConstraints = buildInitialTemporalConstraints(
                initialAndFinalSituations,
                initialAndFinalSteps
        );

        return new Plan(initialAndFinalSituations, initialAndFinalSteps,
                initialCodenotationConstraints, initialTemporalConstraints);
    }

    public static OrganizationalPlan constructInitialPlan(
            Situation initialSituation,
            Goal goal,
            List<Organization> organizations
    ){
        List<PopSituation> initialAndFinalSituations = buildDummySituations();

        Step initialStep = buildInitialStep(initialSituation, organizations);
        Step finalStep = buildFinalStep(goal);
        List<Step> initialAndFinalSteps = new ArrayList<>(List.of(initialStep, finalStep));

        TemporalConstraints initialTemporalConstraints = buildInitialTemporalConstraints(
                initialAndFinalSituations,
                initialAndFinalSteps
        );

        return new OrganizationalPlan(
                initialAndFinalSituations,
                initialAndFinalSteps,
                buildInitialCc(goal, goal.getGoalContext()),
                initialTemporalConstraints,
                organizations
        );
    }

    private static Step buildFinalStep(Goal goal) {
        return new Step(new LogicalInstance(
                finalAction(goal.getGoalPropositions()),
                goal.getGoalContext()
        ));
    }

    /**
     * Builds the initial step of the plan using a set of organizations to define norms
     * @param initialSituation
     * @param organizations
     * @return
     */
    public static Step buildInitialStep(
            Situation initialSituation,
            List<Organization> organizations
    ){
        Action initialAction = initialAction(initialSituation.getContextualPredicates());

        injectAllAssertions(organizations, initialAction);

        LogicalInstance instance = new LogicalInstance(
                initialAction,
                new Context()
        );

        return new Step(instance);
    }

    /**
     * Inject all assertions from a set of organizations into the consequence of some action.
     * @param organizations
     * @param action
     */
    private static void injectAllAssertions(List<Organization> organizations, Action action) {
        getAllAssertions(organizations)
                .stream()
                .map(predicate -> new Atom(false, predicate))
                .forEach(assertion -> {
                    action.getConsequences().getAtoms().add(assertion);
                });
    }

    /**
     * Returns all assertions from all organizations and their respective institutions
     * @param organizations
     * @return
     */
    public static List<Predicate> getAllAssertions(List<Organization> organizations){
        List<Predicate> assertions = new ArrayList<>();

        for (Organization organization : organizations) {
            assertions.addAll(organization.getAssertions());
            assertions.addAll(organization.getInstitution().getAssertions());
        }

        return assertions;
    }

    private static List<PopSituation> buildDummySituations() {
        PopSituation initialSituation = new PopSituation(), finalSituation = new PopSituation();

        List<PopSituation> dummySituations = new ArrayList<>();
        dummySituations.add(initialSituation);
        dummySituations.add(finalSituation);

        return dummySituations;
    }

    /**
     * Returns both the initial step which asserts the initial situation, and the last step which
     * requires the goal as preconditions
     *
     * @param initialSituation : the initial situation to fetch the initial step's consequences
     * @param goal             : the goal to build the preconditions of the final dummy step
     * @return a List of steps, where <b>the element at 0 is the initial step</b> and <b> the
     * element at 1 is the last step</b>
     */
    private static List<Step> buildDummySteps(Situation initialSituation, Goal goal) {
        List<Step> dummySteps = new ArrayList<>();

        LogicalInstance initialStep = new LogicalInstance(
                initialAction(initialSituation.getContextualPredicates()),
                new Context()
        );

        LogicalInstance finalStep = new LogicalInstance(
                finalAction(goal.getGoalPropositions()),
                new Context()
        );

        dummySteps.add(new Step(initialStep));
        dummySteps.add(new Step(finalStep));

        return dummySteps;
    }

    /**
     * Builds the initial Action to assert the propositions of the initial situation
     *
     * @param propositions : the propositions to use as consequences of the step
     * @return an action whose consequences assert the initial situation
     */
    private static Action initialAction(List<ContextualPredicate> propositions) {
        return new Action(
                Keywords.POP_INITIAL_STEP,
                new ActionPrecondition(),
                new ActionConsequence(propositions
                        .stream()
                        .map(proposition -> new Atom(false, proposition.getPredicate()))
                        .collect(Collectors.toList()))
        );
    }

    /**
     * Builds the final action of the plan which requires the goal as preconditions
     *
     * @param propositions : the propositions of the goal to serve as preconditions
     * @return a dummy Action the goal to be met in the final situation of the plan
     */
    private static Action finalAction(List<Atom> propositions) {
        return new Action(
                Keywords.POP_FINAL_STEP,
                new ActionPrecondition(propositions.stream().filter(
                        proposition -> !proposition.getPredicate().getName()
                                .equals(Keywords.CODENOTATION_OPERATOR)).toList()
                ),
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
     *
     * @param situations : the situations to be arranged
     * @param steps      : the steps to be arranged
     * @return a Temporal constraint on the given situations and steps for POP
     */
    private static TemporalConstraints buildInitialTemporalConstraints(
            List<PopSituation> situations,
            List<Step> steps
    ) {
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

    /**
     * Builds the initial codenotation constraints of the plan. If any (non)codenotation is
     * specified in the goal, then it needs to be mapped to a (non) codenotation constraint
     * @param goal : the goal of the planning problem
     * @param goalContext : the context of the goal that we need to set the codenotation constraint
     * @return the initial Codenotation Constraint for POP planning-
     */
    private static CodenotationConstraints buildInitialCc(Goal goal, Context goalContext) {
        List<Codenotation> initialCodenotations = new ArrayList<>();

        goal.getGoalPropositions()
                .stream()
                .filter(proposition -> proposition.getPredicate()
                        .getName()
                        .equals(Keywords.CODENOTATION_OPERATOR))
                .forEach(proposition -> initialCodenotations.add(
                        GoalMapper.toCodenotation(proposition, goalContext))
                );

        return new CodenotationConstraints(initialCodenotations);
    }

}
