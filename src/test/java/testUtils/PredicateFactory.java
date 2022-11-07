package testUtils;

import logic.Predicate;

import java.util.Arrays;

public class PredicateFactory {
    public static Predicate onTable = new Predicate("onTable", Arrays.asList());

    public static Predicate emptyArm = new Predicate("emptyArm", Arrays.asList());

    public static Predicate stackedBlocks = new Predicate("on", Arrays.asList());

    public static Predicate holdBlock = new Predicate("hold", Arrays.asList());

    public static Predicate freeBlock = new Predicate("free", Arrays.asList());
}
