package pop_IT;

import aStar.AStarProblem;
import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.components.Plan;
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
        Plan someInitialPlan = MockPlan.plan_withThreeFreeBlocks_to_stackedBlocks();

        PopPlanningProblem problem = new PopPlanningProblem();
        List<Operator> options = problem.getOptions(someInitialPlan);

        Assertions.assertEquals("take", options.get(0).getName());
        Assertions.assertEquals("take", options.get(1).getName());
        Assertions.assertEquals("take", options.get(2).getName());
    }

    @Test
    public void shouldDetect__anInvalidState(){
        Plan someInvalidState = MockPlan.planWithInvalidCc__hasContradiction();
        Plan anotherInvalidState = MockPlan.planWithInvalidTc();

        AStarProblem problem = new PopPlanningProblem();

        Assertions.assertFalse(problem.isValid(someInvalidState));
        Assertions.assertFalse(problem.isValid(anotherInvalidState));
    }

    // TODO : test open conditions
    @Test
    public void shouldDetect__allOpenConditions(){
        Plan somePlan = MockPlan.plan_withThreeFreeBlocks_to_stackedBlocks();

    }

    @Test
    public void shouldApply__planModificationCorrectly(){

    }
}
