package aStar_planning.pop.utils;

import aStar.Operator;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.TemporalConstraints;
import aStar_planning.pop.mapper.PlanModificationMapper;
import logic.Action;
import logic.CodenotationConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes all the methods used to resolve an open condition within a given plan.
 */
public class OpenConditionResolver {
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
                .filter(precedingStep -> precedingStep
                        .asserts(openCondition.getProposition(), plan.getCc()))
                .map(assertingStep -> assertingStep
                        .getAssertingCodenotations(openCondition.getProposition()))
                .map(codenotations -> PlanModificationMapper.from(codenotations))
                .collect(Collectors.toList());
    }

    /**
     * TODO : determines all the operators which requires some step, called "establisher" to be put
     * before the open condition in order to solve it.
     * @param plan : the plan in which we resolve the open condition
     * @param openCondition : the open condition to resolve
     * @return the list of all possible plan modifications which will resolve the open condition.
     */
    public static List<Operator> byPromotion(Plan plan, OpenCondition openCondition) {
        List<Operator> planModifications = new ArrayList<>();
        CodenotationConstraints changes = new CodenotationConstraints();

        potentialEstablishers(plan,openCondition)
                .stream()
                .filter(establisher -> plan.getCc().wouldBeValid())

        return Arrays.asList(PlanModificationMapper.from(changes));
    }

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
     * @param possibleActions
     * @return the set of possible steps which would allow to solve the flaw
     */
    public static List<Operator> byCreation(Plan plan, OpenCondition openCondition, List<Action>
                                            possibleActions){
        List<Operator> possibleModifications = new ArrayList<>();

        for (Action possibleAction : possibleActions) {
            getAssertingInstances.forEach(instance -> {
                PopSituation newStepEntry = new PopSituation();
                PopSituation newStepExit = new PopSituation();
                Step newStep = new Step(instance);

                possibleModifications.add(PlanModificationMapper.from(
                    Arrays.asList(newStepEntry, newStepExit),
                    newStep,
                    instance.getAssertingCc(),

                ));
            });
        }
        return possibleModifications;
    }

    private TemporalConstraints wrapStep(PopSituation entry, PopSituation exit){

    }


}