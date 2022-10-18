package logic;

public class Constant extends Term{
    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext) {
        return false;
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext, CodenotationConstraints codenotationConstraint) {
        return false;
    }

    @Override
    public Unifiable build(Context context) {
        return null;
    }
}
