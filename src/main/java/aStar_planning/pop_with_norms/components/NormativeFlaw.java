package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NormativeFlaw implements Flaw {
    private Plan plan;
    private RegulativeNorm flawedNorm;
    private PopSituation situation;

    @Override
    public String toString() {
        String flawName = switch(flawedNorm.getDeonticOperator()){
            case OBLIGATION -> "\n\tMISSING OBLIGATION";
            case PROHIBITION -> "\n\tMISSING PROHIBITION";
            case PERMISSION -> "\n\tMISSING PERMISSION";
        };

        return flawName + " : " +
                this.flawedNorm +
                " IN " + this.situation;
    }
}
