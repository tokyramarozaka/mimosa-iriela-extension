package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Predicate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NormativeProposition extends Atom implements NormConsequences {

    public NormativeProposition(boolean isNegation, Predicate predicate) {
        super(isNegation, predicate);
    }

    @Override
    public boolean isApplied(
            OrganizationalPlan plan,
            PopSituation situation,
            CodenotationConstraints cc
    ){
        return plan.isAsserted(new ContextualAtom(new Context(), this), situation, cc);
    }
}
