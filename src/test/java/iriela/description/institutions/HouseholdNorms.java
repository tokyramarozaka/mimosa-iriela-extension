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
    public static Norm obligationHaveFish() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                obligatoryHaveFish_conditions(TermsFactory.X),
                obligatoryHaveFish_consequences(TermsFactory.X)
        );
    }

    public static Norm obligationHaveWood() {
        return new RegulativeNorm(
                DeonticOperator.OBLIGATION,
                conditionsHaveWood(TermsFactory.X),
                consequencesHaveWood(TermsFactory.X)
        );
    }

    public static NormConsequences obligatoryHaveFish_consequences(Term subject) {
        return new NormativeProposition(false, PredicateFactory.haveFish(subject));
    }

    public static NormConditions obligatoryHaveFish_conditions(Term subject) {
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
