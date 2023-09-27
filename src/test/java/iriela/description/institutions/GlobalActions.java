package iriela.description.institutions;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Term;

import java.util.ArrayList;
import java.util.List;

public class GlobalActions {
    // ACTIONS
    public static ActionPrecondition movePreconditions(Term subject, Term currentZone,
                                                       Term nextZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Global.movingObject(subject)),
                new Atom(false, Global.zone(currentZone)),
                new Atom(false, Global.zone(nextZone)),

                new Atom(false, Global.located(subject, currentZone)),
                new Atom(false, Global.areAdjacents(currentZone, nextZone))
        ));
    }

    public static ActionConsequence moveConsequences(Term subject, Term currentZone,
                                                     Term nextZone) {
        return new ActionConsequence(new ArrayList<>(List.of(
                new Atom(true, Global.located(subject, currentZone)),
                new Atom(false, Global.located(subject, nextZone))
        )));
    }

    public static Action move(Term subject, Term zone1, Term zone2) {
        return new Action(
                Global.move,
                movePreconditions(subject, zone1, zone2),
                moveConsequences(subject, zone1, zone2)
        );
    }
}
