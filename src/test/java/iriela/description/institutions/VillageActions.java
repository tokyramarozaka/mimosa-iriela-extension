package iriela.description.institutions;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Term;

import java.util.List;

public class VillageActions {
    public static Action getExploitationLicense(Term subject, Term zone) {
        return new Action(
                Village.getExploitationLicense,
                getExploitationLicense_preconditions(subject, zone),
                getExploitationLicense_consequences(subject)
        );
    }

    public static ActionPrecondition getExploitationLicense_preconditions(Term subject, Term zone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Global.located(subject, zone)),
                new Atom(false, Village.office(zone))
        ));
    }

    public static ActionConsequence getExploitationLicense_consequences(Term subject) {
        return new ActionConsequence(List.of(
                new Atom(false, Village.haveExploitationLicense(subject))
        ));
    }

    public static Action getFishingLicense(Term subject, Term zone) {
        return new Action(
                Village.getFishingLicense,
                getFishingLicense_preconditions(subject, zone),
                getFishingLicense_consequences(subject)
        );
    }


    public static Action reportCut(Term subject) {
        return new Action(
                Village.reportCut,
                reportCut_preconditions(subject),
                reportCut_consequences(subject)
        );
    }

    public static ActionPrecondition getFishingLicense_preconditions(Term subject, Term zone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Global.located(subject, zone)),
                new Atom(false, Village.office(zone))
        ));
    }

    public static ActionConsequence getFishingLicense_consequences(Term subject) {
        return new ActionConsequence(List.of(
                new Atom(false, Village.haveFishingLicense(subject))
        ));
    }
    public static ActionPrecondition reportCut_preconditions(Term subject) {
        return new ActionPrecondition(List.of(
                new Atom(false, Household.haveWood(subject))
        ));
    }

    public static ActionConsequence reportCut_consequences(Term subject) {
        return new ActionConsequence(List.of(
                new Atom(false, Village.filledCutReport(subject))
        ));
    }

    public static Action getFishingNet(Term subject){
        return new Action(
                Village.getFishingNet,
                getFishinNet_preconditions(subject),
                getFishingNet_consequences(subject)
        );
    }

    public static ActionConsequence getFishingNet_consequences(Term subject) {
        return new ActionConsequence(List.of(
            new Atom(false, Village.haveFishingNet(subject))
        ));
    }

    public static ActionPrecondition getFishinNet_preconditions(Term subject) {
        return new ActionPrecondition();
    }
}
