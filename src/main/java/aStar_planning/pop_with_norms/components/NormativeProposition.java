package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Predicate;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

@AllArgsConstructor
public class NormativeProposition extends Atom implements NormConsequences {

    public NormativeProposition(boolean isNegation, Predicate predicate) {
        super(isNegation, predicate);
    }

    @Override
    public boolean isApplied(
            OrganizationalPlan plan,
            PopSituation situation,
            CodenotationConstraints cc,
            Context applicableContext
    ){
        System.out.println("===> " + this.build(applicableContext) + " is asserted in  " +
                situation + " : " + plan.isAsserted(
                new ContextualAtom(applicableContext, this), situation, cc.copy()));

        System.out.println("its preceding steps : " + plan.getSteps().stream()
                .filter(plan::isNotFinalStep)
                .filter(step -> plan.getTc().isBefore(
                        plan.getTc().getFollowingSituation(step),  situation)
                )
                .collect(Collectors.toList()));
        return plan.isAsserted(
                new ContextualAtom(applicableContext, this), situation, cc.copy());
    }
}
