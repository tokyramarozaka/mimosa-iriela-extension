package aStar_planning.pop;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class OpenCondition implements Flaw{
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
