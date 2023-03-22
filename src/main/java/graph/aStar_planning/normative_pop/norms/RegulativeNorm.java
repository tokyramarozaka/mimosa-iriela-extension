package graph.aStar_planning.normative_pop.norms;

import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegulativeNorm extends LogicalEntity implements Norm {
    private String name;
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;

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
}
