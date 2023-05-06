package aStar_planning.normative_pop.utils;

import aStar_planning.normative_pop.components.NormativeFlaw;
import aStar_planning.normative_pop.components.norms.DeonticOperator;
import aStar_planning.pop.components.PlanModification;

import java.util.ArrayList;
import java.util.List;

// TODO : write normative flaw resolver
public class NormativeFlawResolver {
//    public static List<PlanModification> byPromotion(NormativeFlaw normativeFlaw){
//
//    }
//
//    public static List<PlanModification> byDemotion(NormativeFlaw normativeFlaw){
//
//    }
//
//
//    public static List<PlanModification> byCircumvention(NormativeFlaw normativeFlaw){
//
//    }

    /**
     * Resolve a normative flaw by creating a new step which would satisfy the norm
     * @param normativeFlaw : the normative flaw to be resolved
     * @return a list of all plan modifications which would resolve the given (normative) flaw
     */
    public static List<PlanModification> byCreation(NormativeFlaw normativeFlaw){
        if(!normativeFlaw.getFlawedNorm().getDeonticOperator().equals(DeonticOperator.OBLIGATION)){
            return new ArrayList<>();
        }
        if(normativeFlaw.getFlawedNorm().enforceAction()){
            return null; //addObligatoryAction(normativeFlaw.getFlawedNorm());
        }

        return null; // OpenConditionResolver.byCreation(normativeFlaw.getPlan(), )
    }
}
