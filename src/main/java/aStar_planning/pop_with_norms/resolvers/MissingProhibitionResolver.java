package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Action;

import java.util.ArrayList;
import java.util.List;

public class MissingProhibitionResolver {
    public static List<Operator> resolve(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ){
        List<Operator> operators = new ArrayList<>();

        operators.addAll(MissingProhibitionActionResolver.byPromotion(plan, flaw));
        operators.addAll(MissingProhibitionActionResolver.byDemotion(plan, flaw));
        operators.addAll(CircumventionOperator.circumvent(plan, flaw, possibleActions));

        if(operators.isEmpty()){
            System.out.println(" OPERTORS ARE EMPTY ");
        }
        return operators;
    }
}