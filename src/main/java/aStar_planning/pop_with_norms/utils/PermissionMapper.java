package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.RegulativeNorm;

import java.util.ArrayList;
import java.util.List;

public class PermissionMapper {
    public static List<RegulativeNorm> toProhibitions(
            NormativePlan plan,
            RegulativeNorm permission
    ) {
        List<RegulativeNorm> generatedProhibitions = new ArrayList<>();

        plan.getSituations().stream()
                .filter(situation -> !plan.getFinalSituation().equals(situation))
                .forEach(situation -> generatedProhibitions.add(
                        new RegulativeNorm(permission.getName(), DeonticOperator.PROHIBITION,
                                permission.getNormConditions(), permission.getNormConsequences()))
                );
        return generatedProhibitions;
    }
}
