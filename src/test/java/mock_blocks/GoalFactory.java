package mock_blocks;

import logic.Goal;
import logic.Predicate;
import logic.mappers.PredicatesMapper;

import java.util.Arrays;
import java.util.List;

import static mock_blocks.Blocks.A;
import static mock_blocks.Blocks.B;
import static mock_blocks.Blocks.C;
import static mock_blocks.Blocks.X;
import static mock_blocks.Blocks.Y;
import static mock_blocks.Blocks.Z;

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
                new Predicate("on", Arrays.asList(Y,Z))
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
