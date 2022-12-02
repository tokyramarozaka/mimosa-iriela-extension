package aStar_planning.pop;

import aStar.Operator;
import logic.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenConditionResolver {
    public static List<Operator> byCodenotation(OpenCondition openCondition, Plan plan) {
        return plan.getSteps().stream()
                .filter(step -> plan.getTemporalConstraints()
                        .isBefore(step, openCondition.getSituation()))
                .filter(precedingStep -> precedingStep
                        .asserts(openCondition.getProposition(), plan.getCodenotationConstraints()))
                .map(assertingStep -> getAssertingCodenotations(openCondition.getProposition(),
                        plan.getCodenotationConstraints()))
                .map(codenotations -> new PlanModification(codenotations))
                .collect(Collectors.toList());
    }

    public static List<Operator> byDemotion(OpenCondition openCondition, Plan plan) {
        return plan.getSteps().stream()
                .filter(step -> plan
                        .getTemporalConstraints()
                        .isAfter(step, openCondition.getSituation()))
                .filter(step -> step
                        .asserts(openCondition.getProposition(), plan.getCodenotationConstraints()))
                .map(step -> new PlanModification())
                .collect(Collectors.toList());
    }

    public static List<Operator> byCreation(OpenCondition openCondition, Plan plan,
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
