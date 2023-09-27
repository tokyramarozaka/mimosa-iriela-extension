package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Context;

public interface NormConsequences {
    boolean isApplied(NormativePlan plan, PopSituation situation, CodenotationConstraints cc,
                      Context applicableContext);
}