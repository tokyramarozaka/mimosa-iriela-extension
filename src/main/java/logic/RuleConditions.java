package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RuleConditions {
    private List<Atom> conditions;

    public boolean isApplicable(Constraint toTest){
        for (Atom condition : this.conditions) {
            boolean unifiedOnce = false;

            for (ContextualAtom constraint : toTest.getContextualAtoms()) {
                Context conditionContext = new Context();
                if(condition.getPredicate().unify(
                        conditionContext,constraint.getAtom().getPredicate(),constraint.getContext()
                )){
                    unifiedOnce = true;
                    break;
                }
            }

            if (!unifiedOnce){
                return false;
            }
        }
        return true;
    }

    public RuleConditions build(Context context){
        return new RuleConditions(
                this.getConditions().stream().map(condition -> condition.build(context)).toList()
        );
    }
}
