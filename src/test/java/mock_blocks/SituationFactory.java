package mock_blocks;

import logic.Predicate;
import logic.Situation;
import logic.mappers.SituationMapper;

import java.util.Arrays;
import java.util.List;
import static mock_blocks.Blocks.A;
import static mock_blocks.Blocks.B;
import static mock_blocks.Blocks.C;

public class SituationFactory {

    public static Situation threeBlocksOnTable(){
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("onTable", Arrays.asList(A)),
                new Predicate("onTable", Arrays.asList(B)),
                new Predicate("onTable", Arrays.asList(C)),
                new Predicate("free", Arrays.asList(A)),
                new Predicate("free", Arrays.asList(B)),
                new Predicate("free", Arrays.asList(C)),
                new Predicate("emptyArm", Arrays.asList())
        );

        planning.Situation externalSituation = new planning.Situation(predicateList);

        return SituationMapper.convertToInternal(externalSituation);
    }
}
