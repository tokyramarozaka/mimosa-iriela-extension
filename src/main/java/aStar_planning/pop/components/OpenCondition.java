package aStar_planning.pop.components;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenCondition implements Flaw {
    private PopSituation situation;
    private ContextualAtom proposition;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("OPEN CONDITION : ")
                .append(this.proposition)
                .append(" IN ")
                .append(this.situation);

        return stringBuilder.toString();
    }
}
