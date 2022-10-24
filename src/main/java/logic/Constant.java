package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@ToString
@EqualsAndHashCode
public class Constant extends Term{
    public Constant(String name){
        super(name);
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext) {
        return false;
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext, CodenotationConstraints codenotationConstraint) {
        return false;
    }

    @Override
    public boolean unifyBis(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges) {
        return false;
    }

    @Override
    public boolean unifyBis(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges, CodenotationConstraints codenotationConstraints) {
        return false
    }

    @Override
    public Unifiable build(Context context) {
        return this;
    }
}
