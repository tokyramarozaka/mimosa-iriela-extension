package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import constraints.TemporalConstraints;

public class NormativePlanModificationMapper {
    public static PlanModification from(NormativeFlaw flaw, TemporalConstraints tcChanges) {
        return PlanModification
                .builder()
                .targetFlaw(flaw)
                .addedTc(tcChanges)
                .build();
    }
}