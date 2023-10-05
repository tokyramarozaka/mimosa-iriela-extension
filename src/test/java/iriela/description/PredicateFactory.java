package iriela.description;

import logic.Predicate;
import logic.Term;

import java.util.List;

public class PredicateFactory {
    public static Predicate haveFish(Term subject){
        return new Predicate("haveFish", List.of(subject));
    }

    public static Predicate haveWood(Term subject){
        return new Predicate("haveWood", List.of(subject));
    }
}
