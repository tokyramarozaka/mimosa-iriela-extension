package backward_tests.backward_IT;

import aStar.AStarProblem;
import aStar.AStarResolver;
import aStar.Operator;
import aStar_planning.backward.BackwardPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Rule;
import logic.Situation;
import org.junit.jupiter.api.Test;
import mockFactory_forTests.ActionFactory;
import mockFactory_forTests.GoalFactory;
import mockFactory_forTests.RulesFactory;
import mockFactory_forTests.SituationFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlocksWorld_Backward_IT {
    @Test
    public void shouldStackThreeConcreteBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocks_ABC_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        List<Rule> rules = RulesFactory.allRules__BlocksWorld();
        AStarProblem problem = new BackwardPlanningProblem(
                initialSituation,
                possibleActions,
                goal,
                rules
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(),4,"Plan should have 4 actions");
        assertEquals(solutionPlan.get(0).getName(),"take");
        assertEquals(solutionPlan.get(1).getName(),"stack");
        assertEquals(solutionPlan.get(2).getName(),"take");
        assertEquals(solutionPlan.get(3).getName(),"stack");
    }

    @Test
    public void shouldStackThreeAbstractBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.anyThreeBlocks_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        List<Rule> rules = RulesFactory.allRules__BlocksWorld();
        AStarProblem problem = new BackwardPlanningProblem(
                initialSituation,
                possibleActions,
                goal,
                rules
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(),4,"Plan should have 4 actions");
        assertEquals(solutionPlan.get(0).getName(),"take");
        assertEquals(solutionPlan.get(1).getName(),"stack");
        assertEquals(solutionPlan.get(2).getName(),"take");
        assertEquals(solutionPlan.get(3).getName(),"stack");
    }

    @Test
    public void shouldReturnEmptyPlan(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocksOnTable();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        List<Rule> rules = RulesFactory.allRules__BlocksWorld();
        AStarProblem problem = new BackwardPlanningProblem(
                initialSituation,
                possibleActions,
                goal,
                rules
        );

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(),4,"Plan should have 4 actions");
        assertEquals(solutionPlan.get(0).getName(),"take");
        assertEquals(solutionPlan.get(1).getName(),"stack");
        assertEquals(solutionPlan.get(2).getName(),"take");
        assertEquals(solutionPlan.get(3).getName(),"stack");
    }

}
