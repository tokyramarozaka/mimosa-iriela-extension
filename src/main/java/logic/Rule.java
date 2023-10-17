package logic;

import constraints.CodenotationConstraints;
import logic.automated_theorem_proving.ClauseSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Rule extends LogicalEntity{
    private String name;
    private RuleConditions conditions;
    private RuleConclusions conclusions;

    public boolean isApplicable(Constraint toTest){
        return this.conditions.isApplicable(toTest);
    }

    public boolean isApplied(Constraint toTest){
        ClauseSet unitClauses = new ClauseSet();
        ClauseSet compositeClauses = new ClauseSet();

        //unitClauses.add(ConstraintMapper.toClause(toTest));

        toTest.getContextualAtoms().forEach(proposition -> {

        });

        return true;
    }

    @Override
    public LogicalEntity build(Context context) {
        return new Rule(this.name, this.conditions.build(context), this.conclusions.build(context));
    }

    @Override
    public LogicalEntity build(Context context, CodenotationConstraints cc) {
        // TODO : fill this if you want to represent backward using codenotation constraints
        // instead of contexts for variable bindings.
        return null;
    }

    @Override
    public LogicalEntity copy() {
        return null;
    }

    @Override
    public String getLabel() {
        return this.name;
    }
}
