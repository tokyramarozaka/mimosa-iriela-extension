package logic;

import aStar.AStarResolver;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of codenotations constraints to describe variable bindings. They can either be :
 * <ul>
 *     <li>Codenotations forcing the binding between two terms, or,</li>
 *     <li>Non-codenotations forbidding the binding between two terms.</li>
 * </ul>
 *
 * @see logic.Codenotation
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CodenotationConstraints extends Graphic{
    private List<Codenotation> codenotations;

    /**
     * Checks if the current codenotation constraints are coherent : meaning that there is no
     * contradiction, and that there is no auto-binding which would create an infinite cycle.
     *
     * @return true, if the current codenotation constraints are coherent and totally okay, false
     * otherwise.
     */
    public boolean isCoherent() {
        return this.hasNoContradictions() && this.hasNoCycles();
    }

    /**
     * <p>Checks if the set of codenotations are not cyclic, where the variable is linked to itself
     * creating an infinite cycle. This cyclic binding might be direct <i>(ex: x = y)</i> or
     * indirect <i>(x = y; y = x).</i></p>
     * <br>
     *
     * <p> A cycle exists if, given <i>(x = y)</i>, a planning from <i>y</i> to <i>x</i> succeeds in
     * the codenotation graph. </p>
     *
     * @return true if the current codenotation constraints have no cycles, false otherwise.
     */
    private boolean hasNoCycles() {
        List<Codenotation> codenotationsOnly = this.getCodenotations()
                .stream()
                .filter(codenotation -> codenotation.isCodenotation())
                .toList();

        for (Codenotation codenotation : codenotationsOnly) {
            AStarResolver loopPlanningProblem = new AStarResolver(new GraphForwardPlanningProblem(
                    this.getGraph().getContainingNode(codenotation.getRightContextualTerm()),
                    this.getGraph().getContainingNode(codenotation.getLeftContextualTerm())
            ));

            try{
                loopPlanningProblem.findSolution();

                return true;
            }catch(NoPlanFoundException exception){
                return false;
            }
        }

        return false;
    }

    /**
     * TODO : Check if there are any contradictions in the current codenotation constraints
     * @return true if there are any contradictions, false otherwise.
     */
    private boolean hasNoContradictions() {
        List<Codenotation> codenotationsOnly = this.codenotations
                .stream()
                .filter(Codenotation::isCodenotation)
                .collect(Collectors.toList());

        List<Codenotation> nonCodenotationsOnly = this.codenotations
                .stream()
                .filter(codenotation -> codenotation.isCodenotation())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the link of a Variable inside of the codenotation constraints
     * @param variable : the source variable
     * @param context : the source variable's context
     * @return the term linked to the source
     */
    public ContextualTerm getLink(Variable variable, Context context) {
        ContextualTerm source = new ContextualTerm(context,variable);

        if(!isLinked(variable, context)){
            return source;
        }

        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.getLeftContextualTerm().equals(source))
                .findFirst()
                .get()
                .getRightContextualTerm();
    }

    /**
     * Checks if a variable is linked according to the currentCodenotations. Note that
     * non-codenotation constraints does not count as a link.
     * @param variable : the source variable we want to check
     * @param context : the context of the source variable
     * @return true if the variable has is codenotated to another variable, false otherwise.
     */
    public boolean isLinked(Variable variable, Context context) {
        ContextualTerm toTest = new ContextualTerm(context, variable);

        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.getLeftContextualTerm().equals(toTest)
                    && codenotation.isCodenotation())
                .count() > 0;
    }

    /**
     * Removes all bindings of the given term within the current codenotation constraints
     * @param toRemove : the term to remove
     * @return true, if any elements were removed, and false otherwise.
     */
    public boolean unlink(ContextualTerm toRemove){
        return this.getCodenotations().removeIf(
                codenotation -> codenotation.getLeftContextualTerm().equals(toRemove)
        );
    }
    /**
     * Checks if a given (non)codenotation would make the current codenotations contradict itself.
     * TODO : check for indirect conflicts here.
     * @param toAdd : the codenotation we want to check without adding
     * @return true, if the codenotation does not break the current constraint, false otherwise.
     */
    public boolean isAllowed(Codenotation toAdd) {
        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.matches(toAdd)
                        && toAdd.isCodenotation() != toAdd.isCodenotation())
                .count() == 0;
    }

    public CodenotationConstraints copy(){
        return new CodenotationConstraints(
                new ArrayList<>(this.getCodenotations())
        );
    }
}
