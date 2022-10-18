package logic;

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
        return ;
    }

    @Override
    public LogicalEntity build(Context context) {
        return new Rule(this.name, this.conditions.build(context), this.conclusions.build(context));
    }

    @Override
    public LogicalEntity copy() {
        return null;
    }
}
