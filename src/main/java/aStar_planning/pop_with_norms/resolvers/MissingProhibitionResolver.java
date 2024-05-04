package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import logic.Action;

import java.util.ArrayList;
import java.util.List;

public class MissingProhibitionResolver {
    public static List<Operator> resolve(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ) {
        List<Operator> operators = new ArrayList<>();
        RegulativeNorm flawedNorm = flaw.getFlawedNorm();

        if (flawedNorm.enforceAction()) {
            resolveProhibitionAction(plan, flaw, possibleActions, operators);
        } else {
            resolveProhibitionProposition(plan, flaw, possibleActions, operators);
        }

        if (operators.isEmpty()) {
            throw new RuntimeException("Operators are empty, cannot resolve prohibition flaw");
        }
        return operators;
    }

    private static void resolveProhibitionProposition(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions,
            List<Operator> operators
    ) {
        operators.addAll(MissingProhibitionPropositionResolver.byPromotion(plan, flaw));
        operators.addAll(MissingProhibitionPropositionResolver.byDemotion(plan, flaw));
        operators.addAll(CircumventionOperator.circumvent(plan, flaw, possibleActions));
    }

    private static void resolveProhibitionAction(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions,
            List<Operator> operators
    ) {
        operators.addAll(MissingProhibitionActionResolver.byPromotion(plan, flaw));
        operators.addAll(MissingProhibitionActionResolver.byDemotion(plan, flaw));
        operators.addAll(CircumventionOperator.circumvent(plan, flaw, possibleActions));
    }

}