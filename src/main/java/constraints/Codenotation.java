package constraints;

import graph.Node;
import logic.ContextualTerm;
import logic.utils.ContextChange;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
public class Codenotation{
    private boolean isCodenotation;
    private ContextualTerm left;
    private ContextualTerm right;

    public Codenotation updateContext(ContextChange contextChange) {
        return new Codenotation(
                this.isCodenotation,
                contextChange.getReplacement(left),
                contextChange.getReplacement(right)
        );
    }

    /**
     * Checks if this given codenotation matches to another. Meaning that they both have the same
     * left and right terms. Their orders are not considered.
     * @param other : the codenotation to compare this one to.
     * @return true if codenotations matches, and false otherwise.
     */
    public boolean matches(Codenotation other) {
        return (this.left.equals(other.getLeft()) &&
                this.right.equals(other.getRight())) ||
                    ((this.left.equals(other.getRight())) &&
                        (this.right.equals(other.getLeft())));
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

    public Codenotation reverse() {
        return new Codenotation(
                !this.isCodenotation,
                this.getLeft(),
                this.getRight()
        );
    }

    /**
     * Converts this codenotation to nodes for graphs, there are two nodes to be produced to
     * represent the left and right terms of the codenotation.
     * @return a list of nodes representing the codenotation
     */
    public List<Node> toNodes(){
        return List.of(
                new Node(this.getLeft().toString(), new ArrayList<>(), this.getLeft()),
                new Node(this.getRight().toString(), new ArrayList<>(), this.getRight())
        );
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.left)
                .append(isCodenotation ? " == " : " != ")
                .append(this.right)
                .toString();
    }
}
