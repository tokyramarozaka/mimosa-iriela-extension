package iriela.description;

import logic.Predicate;
import logic.Term;

import java.util.List;

public class PredicateFactory {
    public static Predicate haveFood(Term subject){
        return new Predicate("haveFood", List.of(subject));
    }

    public static Predicate haveWood(Term subject){
        return new Predicate("haveWood", List.of(subject));
    }
}
