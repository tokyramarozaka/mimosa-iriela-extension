package logic;

import logic.utils.ContextChange;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


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

    public boolean matches(Codenotation other) {
        return (this.leftContextualTerm.equals(other.getLeftContextualTerm()) &&
                this.rightContextualTerm.equals(other.getRightContextualTerm())) ||
                    ((this.leftContextualTerm.equals(other.getRightContextualTerm())) &&
                        (this.rightContextualTerm.equals(other.getLeftContextualTerm())));
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.leftContextualTerm)
                .append(isCodenotation ? " == " : " != ")
                .append(this.rightContextualTerm)
                .toString();
    }
}
