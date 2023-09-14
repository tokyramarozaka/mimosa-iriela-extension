package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Role;
import aStar_planning.pop_with_norms.concepts.ActionName;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Constant;
import logic.Predicate;
import logic.Term;
import mock_logic.validation_model.TermsFactory;

import java.util.ArrayList;
import java.util.List;

public class Global {
    // CONCEPTS
    public static Role movingObject = new Role("movingObject");
    public static Role zone = new Role("zone");
    public static ActionName move = new ActionName("move");

    // ASSERTIONS
    public static Predicate located(Term object, Term zone) {
        return new Predicate("located", List.of(object, zone));
    }

    public static Predicate areAdjacents(Term zone1, Term zone2) {
        return new Predicate("areAdjacents", List.of(zone1, zone2));
    }


    // ACTIONS
    private static ActionPrecondition movePreconditions(Term subject, Term currentZone,
                                                        Term nextZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Global.movingObject(subject)),
                new Atom(false, Global.zone(currentZone)),
                new Atom(false, Global.zone(nextZone)),

                new Atom(false, Global.located(subject, currentZone)),
                new Atom(false, Global.areAdjacents(currentZone, nextZone))
        ));
    }

    private static ActionConsequence moveConsequences(Term subject, Term currentZone,
                                                      Term nextZone) {
        return new ActionConsequence(List.of(
                new Atom(true, Global.located(subject, currentZone)),
                new Atom(false, Global.located(subject, nextZone))
        ));
    }

    public static Action move(Term subject, Term zone1, Term zone2) {
        return new Action(
                Global.move,
                movePreconditions(subject, zone1, zone2),
                moveConsequences(subject, zone1, zone2)
        );
    }


    // NORMS
    public static List<Norm> norms() {
        return new ArrayList<>();
    }

    // INSTITUTION
    public static Institution get() {
        return new Institution(
                "global",
                10,
                Global.concepts(),
                Global.assertions(),
                Global.actions(),
                Global.norms()
        );
    }

    // ROLE CHECK PREDICATES
    public static Predicate zone(Term subject){
        return new Predicate("zone", List.of(subject));
    }

    public static Predicate movingObject(Term subject){
        return new Predicate("movingObject", List.of(subject));
    }

    // UTILITY LISTS
    public static List<Predicate> assertions(){
        return List.of(
                located(TermsFactory.X, TermsFactory.Y),
                areAdjacents(TermsFactory.X, TermsFactory.Y)
        );
    }

    public static List<Constant> concepts(){
        return List.of(movingObject, zone, move);
    }

    public static List<Action> actions() {
        return List.of(
                move(TermsFactory.X, TermsFactory.Y, TermsFactory.Z)
        );
    }
}
