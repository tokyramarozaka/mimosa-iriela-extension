package mockFactory_forTests;

import logic.Goal;
import logic.Predicate;
import logic.mapper.PredicatesMapper;

import java.util.Arrays;
import java.util.List;

import static mockFactory_forTests.Blocks.A;
import static mockFactory_forTests.Blocks.B;
import static mockFactory_forTests.Blocks.C;
import static mockFactory_forTests.Blocks.X;
import static mockFactory_forTests.Blocks.Y;
import static mockFactory_forTests.Blocks.Z;

public class GoalFactory {
    public static Goal threeBlocks_ABC_stacked(){
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("on",Arrays.asList(A,B)),
                new Predicate("on",Arrays.asList(B,C)),
                new Predicate("onTable", Arrays.asList(C)),
                new Predicate("emptyArm", Arrays.asList())
        );

        return PredicatesMapper.toGoal(predicateList);
    }

    public static Goal anyThreeBlocks_stacked() {
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("on", Arrays.asList(X,Y)),
                new Predicate("on", Arrays.asList(Y,Z)),
                new Predicate("onTable", Arrays.asList(Z))
        );

        return PredicatesMapper.toGoal(predicateList);
    }

    public static Goal threeBlocksOnTable() {
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("onTable",Arrays.asList(A)),
                new Predicate("onTable",Arrays.asList(B)),
                new Predicate("onTable", Arrays.asList(C)),
                new Predicate("emptyArm", Arrays.asList())
        );

        return PredicatesMapper.toGoal(predicateList);
    }
}
