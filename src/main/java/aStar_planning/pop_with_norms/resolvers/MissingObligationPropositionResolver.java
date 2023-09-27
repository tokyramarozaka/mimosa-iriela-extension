package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.resolvers.OpenConditionResolver;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Action;
import logic.ContextualAtom;

import java.util.List;

public class MissingObligationPropositionResolver {
    public static List<Operator> byCreation(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byCreation(plan, toResolve, possibleActions);
    }

    public static List<Operator> byPromotion(NormativePlan plan, NormativeFlaw flaw){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byPromotion(plan, toResolve);
    }

    public static List<Operator> byCodenotation(NormativePlan plan, NormativeFlaw flaw){
        OpenCondition toResolve = createOpenCondition(flaw);

        return OpenConditionResolver.byCodenotation(plan, toResolve);
    }

    private static OpenCondition createOpenCondition(NormativeFlaw flaw) {
        NormativeProposition proposition = (NormativeProposition) flaw.getFlawedNorm()
                .getNormConsequences();
        ContextualAtom toAssert = new ContextualAtom(flaw.getContext(), proposition);

        return new OpenCondition(flaw.getApplicableSituation(), toAssert);
    }
}
