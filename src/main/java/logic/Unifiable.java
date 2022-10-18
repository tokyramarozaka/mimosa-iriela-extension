package logic;


public interface Unifiable {
    boolean unify(Context currentContext, Unifiable other, Context otherContext);

    boolean unify(Context currentContext, Unifiable other, Context otherContext, CodenotationConstraints codenotationConstraint);

    Unifiable build(Context context);
}
