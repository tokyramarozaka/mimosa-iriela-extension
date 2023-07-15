package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.norms.NormativeAction;
import aStar_planning.pop_with_norms.components.norms.NormativeProposition;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NormativeFlaw implements Flaw {
    private NormativePlan plan;
    private RegulativeNorm flawedNorm;
    private PopSituation applicableSituation;

    public boolean enforcesAction(){
        return this.getFlawedNorm().getNormConsequences().getClass().equals(NormativeAction.class);
    }

    public boolean enforcesProposition(){
        return this.getFlawedNorm().getNormConsequences().getClass()
                .equals(NormativeProposition.class);
    }

    @Override
    public String toString() {
        String flawName = switch(flawedNorm.getDeonticOperator()){
            case OBLIGATION -> "\n\tMISSING OBLIGATION";
            case PROHIBITION -> "\n\tMISSING PROHIBITION";
            case PERMISSION -> "\n\tMISSING PERMISSION";
        };

        return flawName + " : " +
                this.flawedNorm +
                " IN " + this.applicableSituation;
    }
}
