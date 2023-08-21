package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.LogicalEntity;

public abstract class Norm extends LogicalEntity {
    public abstract boolean isApplied(NormativePlan plan, PopSituation situation);
}
