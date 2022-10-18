package logic;

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
@ToString
public class Codenotation {
    private boolean isCodenotation;
    private ContextualTerm leftContextualTerm;
    private ContextualTerm rightContextualTerm;
}
