package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.resolvers.OpenConditionResolver;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.OrganizationalPlan;
import logic.Action;
import logic.Context;
import logic.ContextualAtom;

import java.util.List;

public class MissingObligationPropositionResolver {
    public static List<Operator> byCreation(
            OrganizationalPlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byCreation(plan, toResolve, possibleActions);
    }

    public static List<Operator> byPromotion(OrganizationalPlan plan, NormativeFlaw flaw){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byPromotion(plan, toResolve);
    }

    public static List<Operator> byCodenotation(OrganizationalPlan plan, NormativeFlaw flaw){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byCodenotation(plan, toResolve);
    }

    private static OpenCondition createOpenCondition(NormativeFlaw flaw) {
        NormativeProposition proposition = (NormativeProposition) flaw.getFlawedNorm()
                .getNormConsequences();
        ContextualAtom toAssert = new ContextualAtom(new Context(), proposition);

        return new OpenCondition(flaw.getApplicableSituation(), toAssert);
    }
}
