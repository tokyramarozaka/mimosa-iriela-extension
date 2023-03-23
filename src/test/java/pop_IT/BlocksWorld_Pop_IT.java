package pop_IT;

import aStar.AStarResolver;
import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import outputs.PlanningOutput;

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
    public void outputPlan_ok(){
        AStarResolver resolver = new AStarResolver(problem);

        PlanningOutput output = resolver.outputSolutionPlan();
    }

    @Test
    public void outputPlan_ifGoalHasUngroundedVariables_ok(){
        PopPlanningProblem abstractProblem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.anyThreeBlocks_stacked()
        );
        AStarResolver resolver = new AStarResolver(abstractProblem);

        PlanningOutput output = resolver.outputSolutionPlan();
    }
}
