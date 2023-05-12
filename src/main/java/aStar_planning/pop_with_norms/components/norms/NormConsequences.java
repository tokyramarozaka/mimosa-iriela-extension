package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;

public interface NormConsequences {
    boolean isApplied(NormativePlan plan, PopSituation situation);
}