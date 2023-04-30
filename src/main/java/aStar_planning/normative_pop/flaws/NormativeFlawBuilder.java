package aStar_planning.normative_pop.flaws;

import aStar_planning.normative_pop.norms.RegulativeNorm;
import aStar_planning.pop.components.Plan;
import time.Interval;

public class NormativeFlawBuilder {
    public static NormativeFlaw build(Plan plan, RegulativeNorm flawedNorm, Interval interval){
        return new NormativeFlaw(plan, flawedNorm, interval);
    }
}