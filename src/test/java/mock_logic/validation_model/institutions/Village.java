package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.NormConditions;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import aStar_planning.pop_with_norms.components.Role;
import aStar_planning.pop_with_norms.concepts.ActionName;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Constant;
import logic.Predicate;
import logic.Term;
import mock_logic.validation_model.PredicateFactory;
import mock_logic.validation_model.TermsFactory;

import java.util.List;

/**
 * A class to provide all the basic roles present within the model
 */
public class Village {
    // CONCEPTS
    public static final Role member = new Role("member");
    public static final Role land = new Role("land");
    public static final Role forest = new Role("forest");
    public static final Role river = new Role("river");
    public static final Role office = new Role("office");
    public static final Role sacred = new Role("sacred");
    public static final Role _protected = new Role("_protected");
    public static final ActionName fish = new ActionName("fish");
    public static final ActionName cut = new ActionName("cut");


    // ASSERTIONS
    public static Predicate hasTrees(Term zone) {
        return new Predicate("hasTrees", List.of(zone));
    }
    public static Predicate hasFish(Term zone) {
        return new Predicate("hasFish", List.of(zone));
    }


    // ACTIONS
    public static ActionPrecondition fishPreconditions(Term subject, Term someZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Global.located(subject, someZone)),
                new Atom(false, Village.hasFish(someZone)),
                new Atom(false, Village.hasTrees(someZone)),
                new Atom(false, Village.river(someZone)),
                new Atom(false, Global.zone(someZone))
        ));
    }
    public static ActionConsequence fishConsequences(Term subject, Term someZone) {
        return new ActionConsequence(List.of(
                new Atom(true, Village.hasFish(someZone)),
                new Atom(false, PredicateFactory.haveFood(subject)))
        );
    }
    public static Action fish(Term subject, Term zone) {
        return new Action(
                fish,
                fishPreconditions(subject, zone),
                fishConsequences(subject, zone)
        );
    }

    private static ActionPrecondition cutPreconditions(Term subject, Term someZone) {
        return new ActionPrecondition(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Global.zone(someZone)),
                new Atom(false, Village.forest(someZone)),
                new Atom(false, Village.hasTrees(someZone))
        ));
    }
    private static ActionConsequence cutConsequences(Term subject, Term someZone) {
        return new ActionConsequence(List.of(
                new Atom(true, Village.hasTrees(someZone)),
                new Atom(false, PredicateFactory.haveWood(subject))
        ));
    }
    public static Action cut(Term subject, Term zone) {
        return new Action(
                cut,
                cutPreconditions(subject, zone),
                cutConsequences(subject, zone));
    }

    // INSTITUTION
    public static Institution get() {
        return new Institution(
                "village",
                7,
                Village.concepts(),
                Village.assertions(),
                Village.actions(),
                Village.norms()
        );
    }

    // ROLE CHECK PREDICATES
    public static Predicate member(Term subject) {
        return new Predicate("member", List.of(subject));
    }
    public static Predicate land(Term subject){
        return new Predicate ("land", List.of(subject));
    }
    public static Predicate forest(Term subject) {
        return new Predicate("forest", List.of(subject));
    }
    public static Predicate river(Term subject) {
        return new Predicate("river", List.of(subject));
    }
    public static Predicate office(Term subject) {
        return new Predicate("office", List.of(subject));
    }
    public static Predicate sacred(Term subject) {
        return new Predicate("sacred", List.of(subject));
    }
    public static Predicate _protected(Term subject) {
        return new Predicate("_protected", List.of(subject));
    }

    // UTILITY LISTS
    private static List<Norm> norms() {
        return List.of(
                sacredCut(TermsFactory.X, TermsFactory.Z),
                sacredFish(TermsFactory.X, TermsFactory.Z)
        );
    }
    private static List<Action> actions() {
        return List.of(
                fish(TermsFactory.X, TermsFactory.Z),
                cut(TermsFactory.X, TermsFactory.Z)
        );
    }

    private static List<Constant> concepts() {
        return List.of(member, land, forest, river, office, sacred, _protected, fish, cut);
    }

    private static List<Predicate> assertions() {
        return List.of(hasTrees(TermsFactory.X), hasFish(TermsFactory.X));
    }

    // NORMS
    private static Norm sacredFish(Term subject, Term zone) {
        return new RegulativeNorm(
                DeonticOperator.PROHIBITION,
                conditionsSacredFish(subject, zone),
                consequencesSacredFish(subject, zone)
        );
    }
    private static NormConsequences consequencesSacredFish(Term subject, Term zone) {
        return Village.fish(subject, zone);
    }
    private static NormConditions conditionsSacredFish(Term subject, Term zone) {
        return new NormConditions(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Global.zone(zone)),
                new Atom(false, Village.sacred(zone))
        ));
    }
    private static Norm sacredCut(Term subject, Term zone) {
        return new RegulativeNorm(
                DeonticOperator.PROHIBITION,
                conditionsSacredCut(subject, zone),
                consequencesSacredCut(subject, zone)
        );
    }
    private static NormConsequences consequencesSacredCut(Term subject, Term zone) {
        return Village.cut(subject, zone);
    }
    private static NormConditions conditionsSacredCut(Term subject, Term zone) {
        return new NormConditions(List.of(
                new Atom(false, Village.member(subject)),
                new Atom(false, Global.zone(zone)),
                new Atom(false, Village.sacred(zone))
        ));
    }
}
