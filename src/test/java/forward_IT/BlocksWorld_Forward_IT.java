package forward_IT;

import aStar.AStarProblem;
import aStar_planning.forward.ForwardPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import org.junit.jupiter.api.Test;
import testUtils.ActionFactory;
import testUtils.GoalFactory;
import testUtils.SituationFactory;

import java.util.List;

public class BlocksWorld_Forward_IT {
    @Test
    public void shouldStackThreeConcreteBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocks_ABC_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();

        AStarProblem problem = new ForwardPlanningProblem(initialSituation,
                possibleActions, goal);


        problem.
    }

    @Test
    public void shouldReturnEmptyPlan(){

    }

    @Test
    public void shouldStackThreeAbstractBlocks(){

    }

    @Test
    public void shouldFindNoPlan(){

    }
}
