package aStar_planning.normative_pop.flaws;

import aStar_planning.normative_pop.norms.DeonticOperator;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.utils.OpenConditionResolver;

import java.util.ArrayList;
import java.util.List;

// TODO : write normative flaw resolver
public class NormativeFlawResolver {
    public static List<PlanModification> byPromotion(NormativeFlaw normativeFlaw){

    }

    public static List<PlanModification> byDemotion(NormativeFlaw normativeFlaw){

    }


    public static List<PlanModification> byCircumvention(NormativeFlaw normativeFlaw){

    }

    public static List<PlanModification> byCreation(NormativeFlaw normativeFlaw){
        if(!normativeFlaw.getFlawedNorm().getDeonticOperator().equals(DeonticOperator.OBLIGATION)){
            return new ArrayList<>();
        }
        if(normativeFlaw.getFlawedNorm().enforceAction()){
            return addObligatoryAction(normativeFlaw.getFlawedNorm());
        }

        return OpenConditionResolver.byCreation(normativeFlaw.getPlan(), )
    }
}
