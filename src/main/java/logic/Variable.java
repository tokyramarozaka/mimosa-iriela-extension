package logic;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class Variable extends Term{
    public Variable(String name) {
        super(name);
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext) {
        return false;
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext,
                         CodenotationConstraints codenotationConstraint) {

    }

    @Override
    public boolean unifyBis(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges) {

    }

    @Override
    public boolean unifyBis(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges, CodenotationConstraints codenotationConstraints) {

    }

    @Override
    public Unifiable build(Context context) {

    }
}