package logic.mapper;

import logic.CodenotationConstraints;
import logic.Constraint;
import logic.Goal;

public class GoalMapper {
    public static Constraint toConstraint(Goal goal){
        return new Constraint(goal.getPropositions());
    }

    public static CodenotationConstraints toCodenotationConstraint(Goal goal){

    }
}
