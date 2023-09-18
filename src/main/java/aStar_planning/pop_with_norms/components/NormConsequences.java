package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Context;

public interface NormConsequences {
    boolean isApplied(OrganizationalPlan plan, PopSituation situation, CodenotationConstraints cc);
    NormConsequences buildConsequences(Context context);
}