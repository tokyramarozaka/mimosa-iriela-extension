package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import logic.Context;

public interface NormConsequences {
    boolean isApplied(NormativePlan plan, PopSituation situation, CodenotationConstraints cc);

    NormConsequences build(Context context);
}