package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.NormConditions;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeAction;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import logic.Atom;
import logic.Term;

import java.util.List;

public class VillageNorms {
    static Norm obligatoryReportFish(Term subject) {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                obligatoryFishReport_conditions(subject),
                obligatoryFishReport_consequences(subject)
        );
    }

    static Norm prohibitiedTrespassingIfSacred(Term subject, Term zone) {
        return new RegulativeNorm(
                DeonticOperator.PROHIBITION,
                locatedInSacred_conditions(subject, zone),
                locatedInSacred_consequences(subject, zone)
        );
    }
    static Norm prohibitedFishingWithoutLicense(Term subject, Term zone) {
        return new RegulativeNorm(
                DeonticOperator.PROHIBITION,
                fishWithoutLicense_conditions(subject, zone),
                fishWithoutLicense_consequences(subject, zone)
        );
    }

    static Norm permittedCutWithLicense(Term subject, Term zone){
        return new RegulativeNorm(
                DeonticOperator.PERMISSION,
                cutWithLicense_conditions(subject, zone),
                cutWithLicense_consequences(subject, zone)
        );
    }

    static Norm permittedToHaveFishingNet_ifHasLicense(Term subject){
        return new RegulativeNorm(
                DeonticOperator.PERMISSION,
                haveFishingNetWithLicense_conditions(subject),
                haveFishingNetWithLicense_consequences(subject)
        );
    }

    /*
    ======================== NORM CONDITIONS AND CONSEQUENCES =================================
     */
    private static NormConditions obligatoryFishReport_conditions(Term subject){
        return new NormConditions(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Household.haveFish(subject))
        ));
    }

    private static NormConsequences obligatoryFishReport_consequences(Term subject){
        return new NormativeAction(VillageActions.reportFish(subject));
    }
    private static NormConditions locatedInSacred_conditions(Term subject, Term zone) {
        return new NormConditions(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Village.sacred(zone))
        ));
    }

    private static NormConsequences locatedInSacred_consequences(Term subject, Term zone) {
        return new NormativeProposition(false, Global.located(subject, zone));
    }

    private static NormConditions fishWithoutLicense_conditions(Term subject, Term zone) {
        return new NormConditions(List.of(
                new Atom(true, Village.haveFishingLicense(subject)),
                new Atom(false, Village.member(subject)),
                new Atom(false, Village._protected(zone))
//              ,new Atom(false, Global.located(subject, zone))
        ));
    }

    private static NormConsequences fishWithoutLicense_consequences(Term subject, Term zone) {
        return new NormativeAction(HouseholdActions.fish(subject, zone));
    }

    private static NormConditions cutWithLicense_conditions(Term subject, Term zone){
        return new NormConditions(List.of(
                new Atom(false, Village.haveExploitationLicense(subject)),
                new Atom(false, Village._protected(zone))
        ));
    }

    private static NormConsequences cutWithLicense_consequences(Term subject, Term zone){
        return new NormativeAction(HouseholdActions.cut(subject, zone));
    }

    private static NormConditions haveFishingNetWithLicense_conditions(Term subject){
        return new NormConditions(List.of(
                new Atom(false, Village.haveFishingLicense(subject))
        ));
    }

    private static NormConsequences haveFishingNetWithLicense_consequences(Term subject){
        return new NormativeProposition(false, Village.hasFishingNet(subject));
    }
}
