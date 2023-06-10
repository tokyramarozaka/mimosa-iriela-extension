package exception;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.norms.Norm;
import aStar_planning.pop_with_norms.components.norms.NormConditions;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnapplicableNormException extends RuntimeException{
    private NormConditions conditions;
    private PopSituation situation;

    @Override
    public String getMessage() {
        return "The norm conditions "+ this.conditions + "were not applicable in " + situation +".";
    }
}
