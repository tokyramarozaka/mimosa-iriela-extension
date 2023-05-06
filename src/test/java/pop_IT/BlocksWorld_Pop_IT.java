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
    @Test
    public void outputPlan_ifGoalHasUngroundedVariables_ok(){
        PopPlanningProblem abstractProblem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.anyThreeBlocks_stacked__withNonCodenotationConstraints()
        );
        AStarResolver resolver = new AStarResolver(abstractProblem);

        PlanningOutput output = resolver.outputSolutionPlan();
    }
}
