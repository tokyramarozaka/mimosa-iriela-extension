package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import aStar_planning.pop_with_norms.components.norms.NormativeProposition;
import logic.Action;

import java.util.List;

public class MissingObligationResolver {
    public static List<Operator> byCreation(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ){
        NormConsequences normConsequences = flaw.getFlawedNorm().getNormConsequences();
        if(normConsequences.getClass().equals(NormativeProposition.class)){
            return MissingObligationPropositionResolver.byCreation(plan, flaw, possibleActions);
        }
        return MissingObligationActionResolver.byCreation(plan, flaw);
    }
}
