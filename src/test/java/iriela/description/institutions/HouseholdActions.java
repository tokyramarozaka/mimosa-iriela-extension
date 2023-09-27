package iriela.description.institutions;

import iriela.description.PredicateFactory;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Term;

import java.util.ArrayList;
import java.util.List;

public class HouseholdActions {
    // ACTIONS
    public static ActionPrecondition fishPreconditions(Term subject, Term someZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Global.located(subject, someZone)),
                new Atom(false, Household.hasFish(someZone))
        ));
    }

    public static ActionConsequence fishConsequences(Term subject, Term someZone) {
        return new ActionConsequence(new ArrayList<>(List.of(
                new Atom(true, Household.hasFish(someZone)),
                new Atom(false, PredicateFactory.haveFood(subject)))
        ));
    }

    public static Action fish(Term subject, Term zone) {
        return new Action(
                Household.fish,
                fishPreconditions(subject, zone),
                fishConsequences(subject, zone)
        );
    }

    public static ActionPrecondition cutPreconditions(Term subject, Term someZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Global.located(subject, someZone)),
                new Atom(false, Household.hasTrees(someZone))
        ));
    }

    public static ActionConsequence cutConsequences(Term subject, Term someZone) {
        return new ActionConsequence(new ArrayList<>(List.of(
                new Atom(true, Household.hasTrees(someZone)),
                new Atom(false, PredicateFactory.haveWood(subject))
        )));
    }

    public static Action cut(Term subject, Term zone) {
        return new Action(
                Household.cut,
                cutPreconditions(subject, zone),
                cutConsequences(subject, zone));
    }
}
