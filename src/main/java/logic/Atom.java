package logic;

import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * An atom is a precondition which can be either an affirmation or a negation. It requires :
 * <ul>
 *     <li>a boolean : is the atom a negation ? </li>
 *     <li>a Predicate : which fact does this atom is about ? </li>
 * </ul>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Atom implements NormConsequences {
    private boolean negation;
    private Predicate predicate;

    public Atom build(Context context) {
        return new Atom(this.negation,(Predicate) this.predicate.build(context));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(this.isNegation() ? "¬" : "")
                .append(this.predicate);

        return stringBuilder.toString();
    }
}
