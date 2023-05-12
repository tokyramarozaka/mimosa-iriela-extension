package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NormConditions {
    private List<Atom> conditions;

    public NormConditions build(Context context) {
        return new NormConditions(this.conditions.stream()
                .map(atom -> atom.build(context))
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int conditionsCount = 0;
        for (Atom condition : conditions) {
            stringBuilder.append(condition);
            if (conditionsCount++ < this.conditions.size() - 1) {
                stringBuilder.append(" , ");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public boolean isApplicable(NormativePlan plan, PopSituation situation) {
        Context stateContext = new Context();

        for (Atom condition : this.conditions) {
            ContextualAtom conditionInstance = new ContextualAtom(stateContext, condition);
            if(!plan.isAsserted(conditionInstance, situation)){
                return false;
            }
        }

        return true;
    }
}
