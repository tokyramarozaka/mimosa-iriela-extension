package mock_logic.blocks_world;

import logic.Atom;
import logic.Context;
import logic.Goal;
import logic.Predicate;
import logic.mappers.PredicatesMapper;
import settings.Keywords;

import java.util.Arrays;
import java.util.List;

import static mock_logic.blocks_world.Blocks.A;
import static mock_logic.blocks_world.Blocks.B;
import static mock_logic.blocks_world.Blocks.C;
import static mock_logic.blocks_world.Blocks.X;
import static mock_logic.blocks_world.Blocks.Y;
import static mock_logic.blocks_world.Blocks.Z;

public class GoalFactory {
    public static Goal threeBlocks_ABC_stacked(){
        List<Predicate> predicateList = Arrays.asList(
                new Predicate("on",Arrays.asList(A,B)),
                new Predicate("on",Arrays.asList(B,C)),
                new Predicate("onTable", List.of(C)),
                new Predicate("emptyArm", List.of())
        );

        return PredicatesMapper.toGoal(predicateList);
    }

    public static Goal anyThreeBlocks_stacked() {
        List<Atom> atomList = List.of(
                new Atom(false, new Predicate("on", List.of(X,Y))),
                new Atom(false, new Predicate("on", List.of(Y,Z)))
        );

        return new Goal(new Context(), atomList);
    }

    public static Goal anyThreeBlocks_stacked__withNonCodenotationConstraints() {
        List<Atom> atomList = List.of(
                new Atom(false, new Predicate("on", List.of(X,Y))),
                new Atom(false, new Predicate("on", List.of(Y,Z))),
                new Atom(true, new Predicate(Keywords.CODENOTATION_OPERATOR, List.of(X,Y))),
                new Atom(true, new Predicate(Keywords.CODENOTATION_OPERATOR, List.of(Y,Z))),
                new Atom(true, new Predicate(Keywords.CODENOTATION_OPERATOR, List.of(X,Z)))
        );

        return new Goal(new Context(), atomList);
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
