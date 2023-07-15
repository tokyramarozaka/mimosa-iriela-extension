package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePropositionFlaw;
import aStar_planning.pop_with_norms.components.NormativeStepFlaw;
import logic.Context;

public class NormativeFlawMapper {
    public static NormativeStepFlaw toNormativeStepFlaw(NormativeFlaw flaw, Step step){
        return new NormativeStepFlaw(
                flaw.getPlan(),
                flaw.getFlawedNorm(),
                flaw.getApplicableSituation(),
                step
        );
    }

    public static NormativePropositionFlaw toNormativePropositionFlaw(
            NormativeFlaw flaw,
            Context context
    ){
        return new NormativePropositionFlaw(
                flaw.getPlan(),
                flaw.getFlawedNorm(),
                flaw.getApplicableSituation(),
                context
        );
    }
}
