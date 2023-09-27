package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Context;
import logic.LogicalEntity;

public abstract class Norm extends LogicalEntity {
    public abstract boolean isApplied(NormativePlan plan, PopSituation situation,
                                      CodenotationConstraints cc, Context applicableContext);
}
