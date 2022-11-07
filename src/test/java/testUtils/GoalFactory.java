package testUtils;

import logic.Goal;
import logic.Predicate;
import logic.Situation;
import logic.mapper.PredicatesMapper;
import logic.mapper.SituationMapper;

import java.util.Arrays;
import java.util.List;
import static testUtils.Blocks.A;
import static testUtils.Blocks.B;
import static testUtils.Blocks.C;

public class GoalFactory{

    public static Goal threeBlocks_ABC_stacked(){
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("on",Arrays.asList(A,B)),
                new Predicate("on",Arrays.asList(B,C)),
                new Predicate("onTable", Arrays.asList(C)),
                new Predicate("emptyArm", Arrays.asList())
        );

        return new Goal(PredicatesMapper.toContextualAtoms(predicateList));
    }
}
