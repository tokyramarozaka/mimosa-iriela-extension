package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import logic.Context;
import lombok.Getter;

@Getter
public class NormativePropositionFlaw extends NormativeFlaw{
    private Context context;
    public NormativePropositionFlaw(
            NormativePlan plan,
            RegulativeNorm flawedNorm,
            PopSituation situation,
            Context context
    ){
        super(plan, flawedNorm, situation);
        this.context = context;
    }
}
