package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import aStar_planning.pop_with_norms.components.norms.NormativeAction;
import aStar_planning.pop_with_norms.components.norms.NormativeProposition;
import logic.Action;

import java.util.ArrayList;
import java.util.List;

public class MissingProhibitionResolver {
    public static List<Operator> resolve(
            NormativePlan plan,
            NormativeFlaw flaw
    ){
        List<Operator> operators = new ArrayList<>();
        NormConsequences normConsequences = flaw.getFlawedNorm().getNormConsequences();

        if (normConsequences.getClass().equals(NormativeAction.class)) {
            operators.addAll(MissingProhibitionActionResolver.byPromotion(plan, flaw));
            operators.addAll(MissingProhibitionActionResolver.byDemotion(plan, flaw));
        }else{
            operators.addAll(MissingProhibitionPropositionResolver.byPromotion(plan, flaw));
            operators.addAll(MissingProhibitionPropositionResolver.byDemotion(plan, flaw));
        }

        return operators;
    }
}
