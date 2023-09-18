package logic;

import constraints.CodenotationConstraints;
import lombok.EqualsAndHashCode;

import java.util.List;

public class Constant extends Term{
    public Constant(String name){
        super(name);
    }

    @Override
    public boolean attemptUnification(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> currentChanges
    ){
        if (to instanceof Constant){
            return this.sameName((Term) to);
        }
        if (to instanceof Variable){
            return ((Variable)to).attemptUnification(toContext, this, fromContext,currentChanges);
        }

        throw new UnsupportedOperationException(
                "Only variables and constants unifications are supported.");
    }

    /**
     * Attempts an unification of the constant with another term
     * @param fromContext : the context of this constant
     * @param to : the other term we want to unify it with
     * @param toContext : the other term's context
     * @param currentChanges : the current changes accumulated so far
     * @param codenotationConstraints : the bindings used to make the unification happen.
     * @return true if the unification succeeds and false otherwise
     */
    @Override
    public boolean attemptUnification(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> currentChanges,
            CodenotationConstraints codenotationConstraints
    ){
        if (to instanceof Constant){
            return this.sameName((Term) to);
        }
        if (to instanceof Variable){
            return ((Variable)to).attemptUnification(toContext, this, fromContext,
                    currentChanges,codenotationConstraints);
        }

        throw new UnsupportedOperationException(
                "Only variables and constants unifications are supported.");
    }

    @Override
    public boolean testEqual(Context fromContext, Term other, Context otherContext) {
        if (other instanceof Constant)
            return this.sameName(other);
        else if ((other instanceof Variable) && (otherContext.isLinked((Variable)other)))
            return testEqual(
                    fromContext,
                    otherContext.getLink((Variable)other).getTerm(),
                    otherContext.getLink((Variable)other).getContext()
            );
        return false;
    }

    @Override
    public boolean testEqual(Context fromContext, Term other, Context otherContext,
                             CodenotationConstraints cc
    ){
        if (other instanceof Constant) {
            return this.sameName(other);
        }else if ((other instanceof Variable) && (otherContext.isLinked((Variable)other))) {
            return testEqual(
                    fromContext,
                    otherContext.getLink((Variable) other).getTerm(),
                    otherContext.getLink((Variable) other).getContext(),
                    cc
            );
        }
        return false;
    }

    @Override
    public Unifiable build(Context context) {
        return this;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
