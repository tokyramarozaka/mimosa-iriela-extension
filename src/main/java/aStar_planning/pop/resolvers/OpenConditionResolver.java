package aStar_planning.pop.resolvers;

import aStar.Operator;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.utils.TemporalConstraintsBuilder;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
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
     * Resolve an open condition by adding a codenotation constraint to an existing step
     * @param plan : the plan in which we want to resolve the open condition
     * @param openCondition : the open condition we want to resolve describing which proposition is
     *                      not necessarily true in which situation
     * @return the set of plan modifications to solve the given open condition.
     */
    public static List<Operator> byCodenotation(Plan plan, OpenCondition openCondition) {
        return plan.getSteps()
                .stream()
                .filter(step -> plan.getTc().isBefore(step, openCondition.getSituation()))
                .filter(precedingStep -> precedingStep.asserts(openCondition.getProposition(),
                        plan.getCc()))
                .map(assertingStep -> assertingStep.getAssertingCodenotations(
                        openCondition.getProposition()))
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
     * Search through the set of possible actions for any new step to solve the open condition.
     * This method does not include the temporal constraints which defines its partial order in the
     * plan
     * @param openCondition : the open condition we are resolving
     * @param possibleActions : the set of possible actions to choose from
     * @return the set of possible steps which would allow to solve the flaw
     */
    public static List<Operator> byCreation(
            Plan plan,
            OpenCondition openCondition,
            List<Action> possibleActions
    ){
        List<Operator> possibleModifications = new ArrayList<>();

        getSolvingSteps(openCondition, possibleActions).forEach(solvingStep -> {
                PopSituation newStepEntry = new PopSituation();
                PopSituation newStepExit = new PopSituation();
                TemporalConstraints tcChanges = TemporalConstraintsBuilder.insertNewStepBetween(
                        plan,solvingStep,newStepEntry,newStepExit,openCondition.getSituation());

            possibleModifications.add(PlanModificationMapper.from(
                    openCondition,
                    Arrays.asList(newStepEntry, newStepExit),
                    solvingStep,
                    solvingStep.toCodenotation(),
                    tcChanges
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
