package mock_logic.validation_model;

import logic.Predicate;
import logic.Term;
import java.util.ArrayList;
import java.util.List;

public class PredicateFactory {
    public static Predicate haveFood(Term subject){
        return new Predicate("haveFood", List.of(subject));
    }

    public static Predicate haveWood(Term subject){
        return new Predicate("haveWood", List.of(subject));
    }

    public static Predicate areAdjacents(Term someZone, Term someOtherZone) {
        return new Predicate("areNeighbors", List.of(someZone, someOtherZone));
    }


}
