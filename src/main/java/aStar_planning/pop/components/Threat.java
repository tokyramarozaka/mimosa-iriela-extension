package aStar_planning.pop.components;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class Threat implements Flaw {
    private Step destroyer;
    private Step threatened;
    private PopSituation applicableSituation;
    private ContextualAtom proposition;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder
                .append("(T): ")
                .append(this.destroyer)
                .append(" THREATENS ")
                .append(this.proposition)
                .append(" IN ")
                .append(this.threatened)
                .toString();
    }
}
