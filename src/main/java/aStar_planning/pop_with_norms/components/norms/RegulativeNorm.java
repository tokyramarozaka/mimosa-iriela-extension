package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegulativeNorm extends Norm {
    private String name;
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;

    public boolean isApplicable(NormativePlan plan, PopSituation situation){
        return this.normConditions.isApplicable(plan, situation);
    }
    /**
     * Determines whether the regulative norm enforces some action that ought (not) to be done.
     * @return true if the norm enforces some action, and false if it enforces some proposition
     * instead
     */
    public boolean enforceAction(){
        return this.getNormConsequences() instanceof NormativeAction;
    }

    /**
     * Determines whether the regulative norm enforces some proposition that ought (not) to be done.
     * @return true if the norm enforces some proposition, and false if it enforces some action
     * instead
     */
    public boolean enforceProposition(){
        return this.getNormConsequences() instanceof NormativeProposition;
    }

    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        return switch(this.deonticOperator){
            case OBLIGATION -> isObligationApplied(plan, situation);
            case PROHIBITION -> isProhibitionApplied(plan, situation);
            case PERMISSION -> isPermissionApplied(plan, situation);
        };
    }

    // TODO : detect if a permission is applied
    public boolean isPermissionApplied(NormativePlan plan, PopSituation situation) {
        return false;
    }

    // TODO : detect if a prohibition is applied
    public boolean isProhibitionApplied(NormativePlan plan, PopSituation situation) {
        return false;
    }

    // TODO : detect if an obligation is being applied
    public boolean isObligationApplied(NormativePlan plan, PopSituation situation) {
        return this.getNormConsequences().isApplied(plan, situation);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.name)
                .append("(")
                .append(this.deonticOperator)
                .append(")")
                .append(" : ")
                .append("\n\t")
                .append(this.normConditions)
                .append(" -> ")
                .append(this.normConsequences)
                .toString();
    }

    @Override
    public LogicalEntity build(Context context) {
        return null;
    }

    @Override
    public LogicalEntity copy() {
        return null;
    }

    @Override
    public String getLabel() {
        return this.name;
    }


}
