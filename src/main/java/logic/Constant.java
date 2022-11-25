package logic;

import java.util.List;


public class Constant extends Term{
    public Constant(String name){
        super(name);
    }

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext) {
        return false;
    }

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext, CodenotationConstraints codenotationConstraint) {
        return false;
    }

    @Override
    public boolean attemptUnification(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges) {
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
     * TODO : implement the unification of constant with CodenotationConstraints
     * @param fromContext
     * @param to
     * @param toContext
     * @param currentChanges
     * @param codenotationConstraints
     * @return
     */
    @Override
    public boolean attemptUnification(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> currentChanges, CodenotationConstraints codenotationConstraints) {
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
