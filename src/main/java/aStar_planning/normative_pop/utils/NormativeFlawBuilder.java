package aStar_planning.normative_pop.utils;

import aStar_planning.normative_pop.components.NormativeFlaw;
import aStar_planning.normative_pop.components.norms.RegulativeNorm;
import aStar_planning.pop.components.Plan;
import time.Interval;

public class NormativeFlawBuilder {
    public static NormativeFlaw build(Plan plan, RegulativeNorm flawedNorm, Interval interval){
        return new NormativeFlaw(plan, flawedNorm, interval);
    }
}