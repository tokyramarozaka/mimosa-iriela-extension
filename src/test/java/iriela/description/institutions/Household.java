package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Role;
import aStar_planning.pop_with_norms.components.RoleActions;
import aStar_planning.pop_with_norms.concepts.ActionName;
import iriela.description.PredicateFactory;
import iriela.description.TermsFactory;
import logic.Action;
import logic.Constant;
import logic.Predicate;
import logic.Term;

import java.util.List;

public class Household {
    // INSTITUTION
    public static Institution get(){
        return new Institution(
                "household",
                9.5f,
                Household.concepts(),
                Household.assertions(),
                Household.roleActions(),
                Household.norms()
        );
    }

    public static Institution get_haveFishOnly(){
        return new Institution(
                "household",
                9.5f,
                Household.concepts(),
                Household.assertions(),
                Household.roleActions(),
                Household.norms_haveFishOnly()
        );
    }
    private static List<Action> providerActions() {
        return List.of(
                HouseholdActions.cut(TermsFactory.X, TermsFactory.Z),
                HouseholdActions.fish(TermsFactory.X, TermsFactory.Z)
        );
    }

    private static List<Predicate> assertions() {
        return List.of(
                containsTrees(TermsFactory.X),
                containsFishes(TermsFactory.X),
                haveFish(TermsFactory.X),
                haveWood(TermsFactory.X)
        );
    }

    private static List<Constant> concepts() {
        return List.of(provider, land, forest, river, cut, fish);
    }

    // CONCEPTS
    public static Role provider = new Role("provider");
    public static final Role land = new Role("land");
    public static final Role forest = new Role("forest");
    public static final Role river = new Role("river");
    public static final ActionName fish = new ActionName("fish");
    public static final ActionName cut = new ActionName("cut");

    //  ASSERTIONS
    public static Predicate containsTrees(Term zone) {
        return new Predicate("containsTrees", List.of(zone));
    }
    public static Predicate containsFishes(Term zone) {
        return new Predicate("containsFishes", List.of(zone));
    }
    public static Predicate haveFish(Term subject){
        return PredicateFactory.haveFish(subject);
    }
    public static Predicate haveWood(Term subject){
        return PredicateFactory.haveWood(subject);
    }

    public static Predicate provider(Term subject){
        return new Predicate("provider", List.of(subject));
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

    // NORMS
    private static List<Norm> norms() {
        return List.of(
                HouseholdNorms.obligationHaveFish(),
                HouseholdNorms.obligationHaveWood()
        );
    }

    private static List<Norm> norms_haveFishOnly() {
        return List.of(
                HouseholdNorms.obligationHaveFish()
        );
    }
    private static List<RoleActions> roleActions(){
        return List.of(forProvider());
    }

    private static RoleActions forProvider() {
        return new RoleActions(provider, providerActions());
    }

}
