package aStar_planning.pop;

import aStar.Operator;
import logic.Action;
import logic.CodenotationConstraints;
import logic.ContextualAtom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenConditionResolver {
    /**
     * TODO : resovle an open condition by adding a codenotation constraint on an existing step
     * @param plan
     * @param openCondition
     * @return
     */
    public static List<Operator> byCodenotation(Plan plan, OpenCondition openCondition) {
        return plan.getSteps()
                .stream()
                .filter(step -> plan.getTc().isBefore(step, openCondition.getSituation()))
                .filter(precedingStep -> precedingStep
                        .asserts(openCondition.getProposition(), plan.getCc()))
                .map(assertingStep -> assertingCodenotations(plan, openCondition.getProposition()))
                .map(codenotations -> PlanModificationMapper.from(codenotations))
                .collect(Collectors.toList());
    }

    /**
     * TODO : retrieve the codenotations which makes one step assert a given proposition
     * @param plan
     * @param proposition
     * @return
     */
    private static CodenotationConstraints assertingCodenotations(
            Plan plan, ContextualAtom proposition
    ){
        return null;
    }

    /**
     * TODO : determines all the operators which requires some step to be put before the flaw
     * @param openCondition
     * @param plan
     * @return
     */
    public static List<Operator> byDemotion(OpenCondition openCondition, Plan plan) {
        return plan.getSteps()
                .stream()
                .filter(step -> plan
                        .getTc()
                        .isAfter(step, openCondition.getSituation()))
                .filter(step -> step
                        .asserts(openCondition.getProposition(), plan.getCc()))
                .map(step -> PlanModificationMapper.from(temporal))
                .collect(Collectors.toList());
    }

    /**
     * Resolve an open condition by adding a brand-new action instance (step)
     * @param openCondition
     * @param plan
     * @param possibleActions
     * @return
     */
    public static List<Operator> byCreation(Plan plan, OpenCondition openCondition,
                                            List<Action> possibleActions)
    {
        List<Operator> solutions = new ArrayList<>();

        plan.searchSolvingStep(openCondition, possibleActions)
                .forEach(solutionStep -> {
                    solutions.add(PlanModificationMapper.from(solutionStep));
                });

        return solutions;
    }
}
