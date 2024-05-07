package exception;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormConditions;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnapplicableNormException extends RuntimeException {
    private NormConditions conditions;
    private PopSituation situation;

    public UnapplicableNormException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (conditions != null && situation != null) {
            return "The norm conditions " + this.conditions + "were not applicable in " + situation + ".";
        }else{
            return super.getMessage();
        }
    }
}
