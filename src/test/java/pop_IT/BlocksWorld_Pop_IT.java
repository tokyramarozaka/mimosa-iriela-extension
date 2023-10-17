package pop_IT;

import aStar.AStarPlanner;
import aStar_planning.pop.PopPlanningProblem;
import mock_logic.blocks_world.ActionFactory;
import mock_logic.blocks_world.GoalFactory;
import mock_logic.blocks_world.SituationFactory;
import org.junit.jupiter.api.Test;
import outputs.PlanningOutput;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlocksWorld_Pop_IT {
    @Test
    public void outputPlan_ifGoalHasUngroundedVariables_ok() throws IOException {
        PopPlanningProblem abstractProblem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.anyThreeBlocks_stacked__withNonCodenotationConstraints()
        );
        AStarPlanner resolver = new AStarPlanner(abstractProblem);

        PlanningOutput output = resolver.outputSolutionPlan();
    }
}
