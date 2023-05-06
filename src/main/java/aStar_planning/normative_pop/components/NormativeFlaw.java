package aStar_planning.normative_pop.components;

import aStar_planning.normative_pop.components.norms.RegulativeNorm;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import time.Interval;

@AllArgsConstructor
@Getter
public class NormativeFlaw implements Flaw {
    private Plan plan;
    private RegulativeNorm flawedNorm;
    private Interval containingInterval;
}
