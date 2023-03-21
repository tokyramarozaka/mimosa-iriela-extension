package pop_IT;

import aStar.AStarProblem;
import aStar.AStarResolver;
import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.components.Plan;
import logic.Action;
import logic.Constant;
import logic.Goal;
import logic.Situation;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mock_blocks.BlockFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlocksWorld_Pop_IT {
    private static PopPlanningProblem problem;

    @BeforeAll
    public static void initializeProblem(){
        problem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.threeBlocks_ABC_stacked()
        );
    }
    @Test
    public void shouldStackThreeConcreteBlocks_finalOutputTest(){
        AStarResolver resolver = new AStarResolver(problem);

        List<Operator> planModifications = resolver.findSolution();
    }
}
