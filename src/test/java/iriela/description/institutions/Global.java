package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Role;
import aStar_planning.pop_with_norms.components.RoleActions;
import aStar_planning.pop_with_norms.concepts.ActionName;
import iriela.description.TermsFactory;
import logic.Action;
import logic.Constant;
import logic.Predicate;
import logic.Term;

import java.util.ArrayList;
import java.util.List;

public class Global {
    // INSTITUTION
    public static Institution get() {
        return new Institution(
                "global",
                10,
                Global.concepts(),
                Global.assertions(),
                Global.roleActions(),
                Global.norms()
        );
    }

    public static List<Constant> concepts(){
        return List.of(movingObject, zone, move);
    }

    public static List<Predicate> assertions(){
        return List.of(
                located(TermsFactory.X, TermsFactory.Y),
                areAdjacents(TermsFactory.X, TermsFactory.Y)
        );
    }

    public static List<Action> actions() {
        return List.of(GlobalActions.move(TermsFactory.X, TermsFactory.Y, TermsFactory.Z));
    }

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

    // NORMS
    public static List<Norm> norms() {
        return new ArrayList<>();
    }

    // ROLE CHECK PREDICATES
    public static Predicate zone(Term subject){
        return new Predicate("zone", List.of(subject));
    }

    public static Predicate movingObject(Term subject){
        return new Predicate("movingObject", List.of(subject));
    }

    private static List<RoleActions> roleActions(){
        return List.of(forMovingObject());
    }

    private static RoleActions forMovingObject(){
        return new RoleActions(movingObject, actions());
    }
}
