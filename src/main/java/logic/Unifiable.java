package logic;


import constraints.CodenotationConstraints;

public interface Unifiable {
    boolean unify(Context fromContext, Unifiable to, Context toContext);

    boolean unify(Context fromContext, Unifiable to, Context toContext, CodenotationConstraints codenotationConstraint);

    Unifiable build(Context context);
}
