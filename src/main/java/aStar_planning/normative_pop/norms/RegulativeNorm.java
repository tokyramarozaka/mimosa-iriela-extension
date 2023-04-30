package aStar_planning.normative_pop.norms;

import aStar_planning.pop.components.PopSituation;
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

    public boolean enforceAction(){
        return this.getNormConsequences().getClass().equals()
    }

    public boolean enforceProposition(){
        return !enforceAction();
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(this.name)
                .append("(")
                .append(this.deonticOperator)
                .append(")")
                .append(" : ")
                .append("\n\t")
                .append(this.normConditions)
                .append(" -> ")
                .append(this.normConsequences);

        return stringBuilder.toString();
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

    public boolean isApplied(PopSituation situation) {
        return switch(this.deonticOperator){
            case OBLIGATION -> isObligationApplied(situation);
            case PROHIBITION -> isProhibitionApplied(situation);
            case PERMISSION -> isPermissionApplied(situation);
        };
    }

    // TODO : detect if a permission is applied
    private boolean isPermissionApplied(PopSituation situation) {
        return false;
    }

    // TOD : detect if a prohibition is applied
    private boolean isProhibitionApplied(PopSituation situation) {
        return false;
    }

    // TODO : detect if an obligation is being applied
    private boolean isObligationApplied(PopSituation situation) {
        return false;
    }
}
