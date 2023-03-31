package aStar_planning.pop.utils;

import aStar.Operator;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop.components.OpenCondition;
import constraints.PartialOrder;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.TemporalConstraints;
import logic.Action;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.LogicalInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes all the methods used to resolve an open condition within a given plan.
 */
public class OpenConditionResolver {
    private static final Logger logger = LogManager.getLogger(OpenConditionResolver.class);

    /**
     * TODO : resolve an open condition by adding a codenotation constraint to an existing step
     * @param plan : the plan in which we want to resolve the open condition
     * @param openCondition : the open condition we want to resolve describing which proposition is
     *                      not necessarily true in which situation
     * @return the set of plan modifications to solve the given open condition.
     */
    public static List<Operator> byCodenotation(Plan plan, OpenCondition openCondition) {
        return plan.getSteps()
                .stream()
                .filter(step -> plan.getTc().isBefore(step, openCondition.getSituation()))
                .filter(precedingStep -> precedingStep.asserts(
                        openCondition.getProposition(),
                        plan.getCc()))
                .map(assertingStep -> assertingStep
                        .getAssertingCodenotations(openCondition.getProposition()))
                .map(codenotations -> PlanModificationMapper.from(openCondition, codenotations))
                .collect(Collectors.toList());
    }

    /**
     * Determines all the operators which requires some step, called "establisher" to be put before
     * the open condition in order to solve it.
     * @param plan : the plan in which we resolve the open condition
     * @param openCondition : the open condition to resolve
     * @return the list of all possible plan modifications which will resolve the open condition.
     */
    public static List<Operator> byPromotion(Plan plan, OpenCondition openCondition) {
        List<Operator> planModifications = new ArrayList<>();

        potentialEstablishers(plan,openCondition)
            .stream()
            .forEach(potentialEstablisher -> {
                PopSituation situationPostEstablisher = plan.getTc()
                        .getFollowingSituation(potentialEstablisher);

                TemporalConstraints temporalChange = new TemporalConstraints(Arrays.asList(
                    new PartialOrder(situationPostEstablisher, openCondition.getSituation())
                ));
                planModifications.add(PlanModificationMapper.from(openCondition, temporalChange));
            });

        return planModifications;
    }

    /**
     * Get all the potential steps which could produce the given open condition in the plan
     * @param plan : the plan we are working in
     * @param openCondition : the open condition that needs to be resolved
     * @return
     */
    private static List<Step> potentialEstablishers(Plan plan, OpenCondition openCondition){
        return plan.getSteps()
                .stream()
                .filter(step -> plan.getTc().isAfter(step, openCondition.getSituation()))
                .filter(step -> step.asserts(openCondition.getProposition(), plan.getCc()))
                .collect(Collectors.toList());
    }
    /**
     * Search through the set of possible actions for any new step to solve the open condition.
     * This method does not include the temporal constraints which defines its partial order in the
     * plan
     * @param openCondition : the open condition we are resolving
     * @param possibleActions : the set of possible actions to choose from
     * @return the set of possible steps which would allow to solve the flaw
     */
    public static List<Operator> byCreation(
            Plan plan,
            OpenCondition openCondition, List<Action>
            possibleActions
    ){
        List<Operator> possibleModifications = new ArrayList<>();

        getSolvingSteps(openCondition, possibleActions).forEach(solvingStep -> {
                PopSituation newStepEntry = new PopSituation();
                PopSituation newStepExit = new PopSituation();

                possibleModifications.add(PlanModificationMapper.from(
                    openCondition,
                    Arrays.asList(newStepEntry, newStepExit),
                    solvingStep,
                    solvingStep.toCodenotation(plan.getCc()),
                    insertNewStepBetween(plan,solvingStep,newStepEntry,newStepExit,openCondition)
                ));
        });

        return possibleModifications;
    }

    /**
     * Returns the list of all steps we could create from the set of possible actions to resolve
     * a given precondition
     * @param openCondition : the open condition we are aiming to resolve
     * @param possibleActions : the set of possible actions for the agent at the current moment
     * @return : the list of steps created to solve a given open condition
     */
    public static List<Step> getSolvingSteps(
            OpenCondition openCondition,
            List<Action> possibleActions
    ){
        List<Step> solvingSteps = new ArrayList<>();

        possibleActions.forEach(possibleAction -> {
            getAssertingInstances(possibleAction, openCondition).forEach(instance -> {
                solvingSteps.add(new Step(instance));
            });
        });

        return solvingSteps;
    }

    /**
     * Provides the necessary temporal constraints to add a new step to insert into the plan, with
     * its wrapping steps (before and after the step). The added step must be added before the
     * new step.
     * @param newStep : the step to insert into the plan
     * @param newStepEntry : the situation preceding the new step
     * @param newStepExit : the situation following the new step
     * @param openCondition : the flaw we want to resolve.
     * @return the temporal constraints needed to insert a step and its wrapping situations
     */
    private static TemporalConstraints insertNewStepBetween(
            Plan plan,
            Step newStep,
            PopSituation newStepEntry,
            PopSituation newStepExit,
            OpenCondition openCondition
    ){
        List<PartialOrder> partialOrders = new ArrayList<>();

        // Adds the temporal constraints of the step and its entry and exit situations
        partialOrders.addAll(wrapStep(newStep, newStepEntry, newStepExit));

        // Adds the exit situation before the following step
        partialOrders.addAll(TemporalConstraintsBuilder.placeBefore(
                newStepExit,
                openCondition.getSituation())
        );

        // Put the entry situation before the new step's entry situation, to link it all together
        partialOrders.add(new PartialOrder(
            plan.getTc().getFollowingSituation(plan.getInitialStep()), newStepEntry
        ));

        return new TemporalConstraints(partialOrders);
    }

    /**
     * Wraps a step with a situation preceding the step, and another following it.
     * @param toWrap : the step to wrap situations around;
     * @param entry : the situation before the step
     * @param exit : the situation after the step
     * @return the temporal orders describing that both situations are placed before and after
     * the step.
     */
    private static List<PartialOrder> wrapStep(Step toWrap, PopSituation entry, PopSituation exit){
        return Arrays.asList(
                new PartialOrder(entry, toWrap),
                new PartialOrder(toWrap, exit)
        );
    }

    /**
     * Returns all the instances of a given action which would resolve an open condition by
     * asserting the missing precondition
     * @param action : the action we want to seek the asserting instances
     * @param openCondition : the given open condition we want to resolve
     * @return the list of all action instances resolving the given open condition
     */
    public static List<LogicalInstance> getAssertingInstances(
            Action action,
            OpenCondition openCondition
    ){
        List<LogicalInstance> assertingInstances = new ArrayList<>();
        ContextualAtom toAssert = openCondition.getProposition();
        Context temp = new Context();

        for(Atom consequence : action.getConsequences().getAtoms()) {
            if (toAssert.getAtom().isNegation() == consequence.isNegation() &&
                    consequence.getPredicate().unify(
                            temp,
                            toAssert.getAtom().getPredicate(),
                            toAssert.getContext())
            ){
                assertingInstances.add(new LogicalInstance(action, temp));
            }
        }

        return assertingInstances;
    }
}
