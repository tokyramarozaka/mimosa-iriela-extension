package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;

public class NormativeProposition extends ContextualAtom implements NormConsequences{
    public NormativeProposition(Context context, Atom atom) {
        super(context, atom);
    }

    public boolean isApplied(NormativePlan plan, PopSituation situation, CodenotationConstraints cc) {
        return plan.isAsserted(new ContextualAtom(this.getContext(), this.getAtom()), situation,cc);
    }

    public NormConsequences build(Context context) {
        return new NormativeProposition(this.getContext(), this.getAtom().build(context));
    }
}
