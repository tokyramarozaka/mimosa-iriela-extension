package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop.components.Plan;

public class NormativeFlawBuilder {
    public static NormativeFlaw build(Plan plan, RegulativeNorm flawedNorm, PopSituation situation){
        return new NormativeFlaw(plan, flawedNorm, situation);
    }
}