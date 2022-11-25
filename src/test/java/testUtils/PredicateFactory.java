package testUtils;

import logic.Predicate;
import logic.Term;

import java.util.Arrays;

public class PredicateFactory {
    public static Predicate onTable(Term block) {
        return new Predicate("onTable", Arrays.asList(block));
    }

    public static Predicate emptyArm = new Predicate("emptyArm", Arrays.asList());

    public static Predicate on(Term block1, Term block2) {
        return new Predicate("on", Arrays.asList(block1, block2));
    }

    public static Predicate hold(Term block) {
        return new Predicate("hold", Arrays.asList(block));
    }

    public static Predicate free(Term block) {
        return new Predicate("free", Arrays.asList(block));
    }
}
