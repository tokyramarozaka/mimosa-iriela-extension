package mock_logic.validation_model;

import logic.Constant;
import logic.Predicate;
import logic.Term;
import java.util.ArrayList;
import java.util.List;

public class PredicateFactory {
    public static Predicate haveFood(){
        return new Predicate("haveFood", new ArrayList<>());
    }

    public static Predicate haveCoal(){
        return new Predicate("haveCoal", new ArrayList<>());
    }

    public static Predicate inForest(Term someForest) {
        return new Predicate("inForest", List.of(someForest));
    }

    public static Predicate inZone(Term someZone) {
        return new Predicate("inZone", List.of(someZone));
    }

    public static Predicate isProtected(Term someZone) {
        return new Predicate("isProtected", List.of(someZone));
    }

    public static Predicate isForest(Term someZone) {
        return new Predicate("isForest", List.of(someZone));
    }

    public static Predicate isSacred(Term someZone) {
        return new Predicate("isSacred", List.of(someZone));
    }

    public static Predicate isOffice(Term someZone) {
        return new Predicate("isOffice", List.of(someZone));
    }

    public static Predicate isNormal(Term someZone){
        return new Predicate("isNormal", List.of(someZone));
    }

    public static Predicate areNeighbors(Term someZone, Term someOtherZone) {
        return new Predicate("areNeighbors", List.of(someZone, someOtherZone));
    }

    public static Predicate haveLicense() {
        return new Predicate("haveLicense", new ArrayList<>());
    }

    public static Predicate haveMoney() {
        return new Predicate("haveMoney", List.of());
    }

    public static Predicate isRiver(Term someZone) {
        return new Predicate("isRiver", List.of(someZone));
    }
}
