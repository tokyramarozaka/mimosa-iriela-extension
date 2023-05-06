package logic.mappers;

import constraints.Codenotation;
import logic.Atom;
import logic.Constraint;
import logic.Context;
import logic.ContextualAtom;
import logic.ContextualTerm;
import logic.Goal;
import logic.Term;
import settings.Keywords;

import java.util.stream.Collectors;

public class GoalMapper {
    /**
     * Converts a goal into a Constraint which is used as the initial state for Backward planning
     * @param goal : the goal to be converted
     * @return the Goal converted into a constraint
     */
    public static Constraint toConstraint(Goal goal){
        return new Constraint(goal.getGoalPropositions()
                .stream()
                .map(proposition -> new ContextualAtom(new Context(), proposition))
                .collect(Collectors.toList()));
    }

    /**
     * Converts an atom from the goal which states that a variable is bound to some other variable
     * into a codenotation constraint. To distinct variable bindings in the goal, we use a special
     * predicate which is displayed in the settings.Keyword class.
     * @see Keywords CODENOTATION_OPERATOR for the binding keyword (default : <b>is(x,y)</b>)
     * @param proposition : the proposition to be converted
     * @param goalContext : the context or identifier of the proposition
     * @return a new Codenotation that links
     */
    public static Codenotation toCodenotation(Atom proposition, Context goalContext) {
        if (!proposition.getPredicate().getName().equals(Keywords.CODENOTATION_OPERATOR)) {
            return null;
        }

        Term left = proposition.getPredicate().getTerms().get(0);
        Term right = proposition.getPredicate().getTerms().get(1);

        return new Codenotation(
                !proposition.isNegation(),
                new ContextualTerm(goalContext, left),
                new ContextualTerm(goalContext, right)
        );
    }
}
