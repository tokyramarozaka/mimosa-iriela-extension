package logic.mappers;

import constraints.CodenotationConstraints;
import logic.Constraint;
import logic.Context;
import logic.ContextualAtom;
import logic.Goal;

import java.util.stream.Collectors;

public class GoalMapper {
    public static Constraint toConstraint(Goal goal){
        return new Constraint(goal.getGoalPropositions()
                .stream()
                .map(proposition -> new ContextualAtom(new Context(), proposition))
                .collect(Collectors.toList()));
    }

    /**
     * TODO : retrieve all codenotationConstraints from a goal
     * @param goal
     * @return
     */
    public static CodenotationConstraints toCodenotationConstraint(Goal goal){
        return null;
    }
}
