package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import lombok.Getter;

@Getter
public class NormativeStepFlaw extends NormativeFlaw implements Interval{
    private Step step;

    public NormativeStepFlaw(
            NormativePlan plan,
            RegulativeNorm flawedNorm,
            PopSituation applicableSituation,
            Step step
    ){
        super(plan, flawedNorm, applicableSituation);
        this.step = step;
    }

    @Override
    public PopSituation getBeginningSituation() {
        return this.getApplicableSituation();
    }

    @Override
    public PopSituation getEndingSituation() {
        // TODO : compute the unapplicable situation : see if there can be multiples
        return null;
    }
}
