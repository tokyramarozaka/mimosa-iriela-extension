package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.NormConditions;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import iriela.description.PredicateFactory;
import iriela.description.TermsFactory;
import logic.Atom;
import logic.Term;

import java.util.List;

public class HouseholdNorms {
    public static Norm obligationHaveFood() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                conditionsHaveFood(TermsFactory.X),
                consequencesHaveFood(TermsFactory.X)
        );
    }

    public static Norm obligationHaveWood() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                conditionsHaveWood(TermsFactory.X),
                consequencesHaveWood(TermsFactory.X)
        );
    }

    public static NormConsequences consequencesHaveFood(Term subject) {
        return new NormativeProposition(false, PredicateFactory.haveFood(subject));
    }

    public static NormConditions conditionsHaveFood(Term subject) {
        return new NormConditions(List.of(
                new Atom(false, Household.provider(subject))
        ));
    }


    public static NormConditions conditionsHaveWood(Term subject) {
        return new NormConditions(List.of(
                new Atom(false, Household.provider(subject))
        ));
    }

    public static NormConsequences consequencesHaveWood(Term subject) {
        return new NormativeProposition(false,PredicateFactory.haveWood(subject));
    }

}
