package constraints;

import logic.ContextualTerm;
import logic.utils.ContextChange;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * A codenotation is a binding between a variable and another term. It defines :
 * <ul>
 *     <li>the type of the binding (a boolean) : describing either the variable must or must not
 *     be bound to a certain term</li>
 *     <li>the left term : which is surely a variable, as constants cannot be bound </li>
 *     <li>the right term : another term which is being bound to the left variable </li>
 * </ul>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Codenotation {
    private boolean isCodenotation;
    private ContextualTerm leftContextualTerm;
    private ContextualTerm rightContextualTerm;

    public Codenotation updateContext(ContextChange contextChange) {
        return new Codenotation(
                this.isCodenotation,
                contextChange.getReplacement(leftContextualTerm),
                contextChange.getReplacement(rightContextualTerm)
        );
    }

    /**
     * Checks if this given codenotation matches to another. Meaning that they both have the same
     * left and right terms. Their orders are not considered.
     * @param other : the codenotation to compare this one to.
     * @return true if codenotations matches, and false otherwise.
     */
    public boolean matches(Codenotation other) {
        return (this.leftContextualTerm.equals(other.getLeftContextualTerm()) &&
                this.rightContextualTerm.equals(other.getRightContextualTerm())) ||
                    ((this.leftContextualTerm.equals(other.getRightContextualTerm())) &&
                        (this.rightContextualTerm.equals(other.getLeftContextualTerm())));
    }

    /**
     * Checks if any of the codenotations from a codenotation list matches the current codenotation
     * @param other : an array list of codenotation we want to check for potential matching
     * @return true if any of the codenotations from the list (<i>other</i>) matches, and false
     * otherwise.
     */
    public boolean matchesAny(List<Codenotation> other){
        return other.stream()
                .filter(codenotation -> codenotation.matches(this))
                .count() > 0;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.leftContextualTerm)
                .append(isCodenotation ? " == " : " != ")
                .append(this.rightContextualTerm)
                .toString();
    }

    public Codenotation reverse() {
        return new Codenotation(!this.isCodenotation, this.getLeftContextualTerm(),
                this.getRightContextualTerm());
    }
}
