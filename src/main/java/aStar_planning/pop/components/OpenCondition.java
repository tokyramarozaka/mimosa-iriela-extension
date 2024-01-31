package aStar_planning.pop.components;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class OpenCondition implements Flaw {
    private PopSituation applicableSituation;
    private ContextualAtom proposition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenCondition that = (OpenCondition) o;
        return Objects.equals(applicableSituation, that.applicableSituation)
                && Objects.equals(proposition, that.proposition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicableSituation, proposition);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("OPEN CONDITION: ")
                .append(this.proposition)
                .append(" IN ")
                .append(this.applicableSituation);

        return stringBuilder.toString();
    }

    public boolean involvesRole() {
        return false;
    }
}
