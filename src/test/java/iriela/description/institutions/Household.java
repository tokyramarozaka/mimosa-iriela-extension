package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.NormConditions;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import aStar_planning.pop_with_norms.components.Role;
import iriela.description.PredicateFactory;
import iriela.description.TermsFactory;
import logic.Action;
import logic.Atom;
import logic.Constant;
import logic.Predicate;
import logic.Term;

import java.util.ArrayList;
import java.util.List;

public class Household {
    // CONCEPTS
    public static Role provider = new Role("provider");

    private static List<Norm> norms() {
        return List.of(
                Household.obligationHaveFood(),
                Household.obligationHaveWood()
        );
    }

    private static NormConsequences consequencesHaveFood(Term subject) {
        return new NormativeProposition(false, PredicateFactory.haveFood(subject));
    }
    private static NormConditions conditionsHaveFood(Term subject) {
        return new NormConditions(List.of(
                new Atom(false, Household.provider(subject))
        ));
    }
    private static Norm obligationHaveFood() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                conditionsHaveFood(TermsFactory.X),
                consequencesHaveFood(TermsFactory.X)
        );
    }

    private static NormConditions conditionsHaveWood(Term subject) {
        return new NormConditions(List.of(
                new Atom(false, Household.provider(subject))
        ));
    }
    private static NormConsequences consequencesHaveWood(Term subject) {
        return new NormativeProposition(false,PredicateFactory.haveWood(subject));
    }
    private static Norm obligationHaveWood() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                conditionsHaveWood(TermsFactory.X),
                consequencesHaveWood(TermsFactory.Y)
        );
    }


    // INSTITUTION
    public static Institution get(){
        return new Institution(
                "household",
                9.5f,
                Household.concepts(),
                Household.assertions(),
                Household.actions(),
                Household.norms()
        );
    }

    public static Predicate provider(Term subject){
        return new Predicate("provider", List.of(subject));
    }

    private static List<Action> actions() {
        return new ArrayList<>();
    }

    private static List<Predicate> assertions() {
        return new ArrayList<>();
    }

    private static List<Constant> concepts() {
        return List.of(provider);
    }
}
