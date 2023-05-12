package mock_logic.blocks_world;

import logic.Predicate;
import logic.Situation;
import logic.mappers.SituationMapper;

import java.util.Arrays;
import java.util.List;
import static mock_logic.blocks_world.Blocks.A;
import static mock_logic.blocks_world.Blocks.B;
import static mock_logic.blocks_world.Blocks.C;

public class SituationFactory {

    public static Situation threeBlocksOnTable(){
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("onTable", List.of(A)),
                new Predicate("onTable", List.of(B)),
                new Predicate("onTable", List.of(C)),
                new Predicate("free", List.of(A)),
                new Predicate("free", List.of(B)),
                new Predicate("free", List.of(C)),
                new Predicate("emptyArm", List.of())
        );

        planning.Situation externalSituation = new planning.Situation(predicateList);

        return SituationMapper.convertToInternal(externalSituation);
    }
}
