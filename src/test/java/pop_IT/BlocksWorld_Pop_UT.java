package pop_IT;

import aStar.AStarProblem;
import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pop_IT.mock.MockPlan;

import java.util.List;

/**
 * TODO : Unit tests on Partial-Order Planning using blocks world
 */
public class BlocksWorld_Pop_UT {
    @Test
    public void shouldDetect__threeTakes_asOptionsWhen_onTable_ABC(){
        Plan someInitialPlan = MockPlan.someInitialPlan();

        PopPlanningProblem problem = new PopPlanningProblem();
        List<Operator> options = problem.getOptions(someInitialPlan);

        Assertions.assertTrue(options.containsAll(take1, take2, take3));
    }

    @Test
    public void shouldDetect__anInvalidState(){
        Plan someInvalidState = MockPlan.planWithInvalidCc();
        Plan anotherInvalidState = MockPlan.planWithInvalidTc();

        AStarProblem problem = new PopPlanningProblem();

        Assertions.assertFalse(problem.isValid(someInvalidState));
        Assertions.assertFalse(problem.isValid(anotherInvalidState));
    }

    @Test
    public void shouldDetect__allOpenConditions(){

    }

    @Test
    public void shouldDetect__allThreats(){

    }

    @Test
    public void shouldDetect__anExecutablePlan(){
        Plan someExecutablePlan = MockPlan.executablePlanWith_ABC_stacked();

        AStarProblem problem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.threeBlocks_ABC_stacked()
        );

        Assertions.assertTrue(problem.isFinal(someExecutablePlan));
    }

    @Test
    public void shouldApply__planModificationCorrectly(){

    }
}
