package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import logic.Action;

import java.util.ArrayList;
import java.util.List;

public class MissingObligationResolver {
    public static List<Operator> resolve(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ){
        List<Operator> operators = new ArrayList<>();
        NormConsequences normConsequences = flaw.getFlawedNorm().getNormConsequences();

        if(normConsequences.getClass().equals(NormativeProposition.class)){
            operators.addAll(MissingObligationPropositionResolver.byCreation(plan, flaw,
                    possibleActions));
        }
        operators.addAll(MissingObligationActionResolver.byCreation(plan, flaw));
        operators.addAll(MissingObligationActionResolver.byPromotion(plan, flaw));

        operators.addAll(CircumventionOperator.circumvent(plan, flaw, possibleActions));

        return operators;
    }
}
