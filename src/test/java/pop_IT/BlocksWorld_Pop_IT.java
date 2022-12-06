package pop_IT;

import aStar.AStarProblem;
import aStar.AStarResolver;
import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import logic.Action;
import logic.Constant;
import logic.Goal;
import logic.Situation;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;
import org.junit.jupiter.api.Test;
import mock_blocks.BlockFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlocksWorld_Pop_IT {
    /**
     * TODO : write the correct assertions for these tests
     */
    @Test
    public void shouldStackThreeConcreteBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocks_ABC_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new PopPlanningProblem(
                initialSituation,
                possibleActions,
                goal
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.

    }

    @Test
    public void shouldStackThreeAbstractBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.anyThreeBlocks_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new PopPlanningProblem(
                initialSituation,
                possibleActions,
                goal
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();
    }


    @Test
    public void shouldReturnEmptyPlan(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocksOnTable();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new PopPlanningProblem(
                initialSituation,
                possibleActions,
                goal
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();
    }

    @Test
    public void shouldRecognizeInvalidPlan(){

    }

    @Test
    public void should
}
