package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePropositionFlaw;
import aStar_planning.pop_with_norms.components.NormativeStepFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import aStar_planning.pop_with_norms.utils.NormativeFlawMapper;
import exception.FlawMismatchException;

import java.util.ArrayList;
import java.util.List;

public class MissingProhibitionResolver {
    public static List<Operator> resolve(
            NormativePlan plan,
            NormativeStepFlaw flaw
    ){
        List<Operator> operators = new ArrayList<>();

        if (!flaw.getFlawedNorm().enforceAction()) {
            throw new FlawMismatchException();
        }

        operators.addAll(MissingProhibitionActionResolver.byPromotion(flaw));
        operators.addAll(MissingProhibitionActionResolver.byDemotion(flaw));

        return operators;
    }

    public static List<Operator> resolve(
            NormativePlan plan,
            NormativePropositionFlaw flaw
    ){
        List<Operator> operators = new ArrayList<>();

        if(!flaw.getFlawedNorm().enforceProposition()){
            throw new FlawMismatchException();
        }

        operators.addAll(MissingProhibitionPropositionResolver.byPromotion(plan, flaw));
        operators.addAll(MissingProhibitionPropositionResolver.byDemotion(plan, flaw));

        return operators;
    }
}