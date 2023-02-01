package pop_IT;

import aStar.Operator;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.PredicateFactory;
import mock_blocks.SituationFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pop_IT.mock.MockPlan;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BlocksWorld_Pop_UT {
    private static PopPlanningProblem problem;
    private final static Logger logger = LogManager.getLogger(BlocksWorld_Pop_UT.class);

    @BeforeAll
    public static void initializeProblem(){
        problem = new PopPlanningProblem(
                SituationFactory.threeBlocksOnTable(),
                ActionFactory.allActionsInBlocksWorld(),
                GoalFactory.threeBlocks_ABC_stacked()
        );
    }

    @Test
    public void assertProposition_ok(){
        Plan initialPlan = problem.getInitialPlan();
        ContextualAtom toAssert_onTableC = getOnTableProposition(initialPlan, "C");
        ContextualAtom toAssert_emptyArm = new ContextualAtom(
                new Context(),
                new Atom(false, PredicateFactory.emptyArm)
        );
        PopSituation finalSituation = getFinalSituation(initialPlan);

        assertTrue(initialPlan.isAsserted(toAssert_onTableC, finalSituation));
        assertTrue(initialPlan.isAsserted(toAssert_emptyArm, finalSituation));
    }

    @Test
    public void shouldDetect__allOpenConditions(){
        Plan initialPlan = problem.getInitialPlan();
        Set<Flaw> initialFlaws = initialPlan.getFlaws();

        assertEquals(2, initialFlaws.size(), "detected the wrong amount of flaws");
    }

    @Test
    public void shouldDetect__twoStackings_asOptionsWhen_ABC_onTable(){
        Plan someInitialPlan = MockPlan.plan_withThreeFreeBlocks_to_stackedBlocks();
        List<Operator> options = problem.getOptions(someInitialPlan);

        List<String> addedStepNames = options.stream()
                .map(operator -> ((PlanModification) operator))
                .map(PlanModification::getAddedStep)
                .filter(Objects::nonNull)
                .map(nonNullStep -> nonNullStep.getActionInstance().getName())
                .toList();

        assertEquals(2, options.size(), "did not find exactly 2 options.");
        assertEquals("stack", addedStepNames.get(0));
        assertEquals("stack", addedStepNames.get(1));
    }

    @Test
    public void shouldDetect__anInvalidState(){
        Plan someInvalidState = MockPlan.planWithInvalidCc__hasContradiction();
        Plan anotherInvalidState = MockPlan.planWithInvalidTc();

        assertFalse(problem.isValid(someInvalidState));
        assertFalse(problem.isValid(anotherInvalidState));
    }

    @Test
    public void shouldApply__planModificationCorrectly(){
        Plan someInitialPlan = problem.getInitialPlan();
        List<Operator> options = problem.getOptions(someInitialPlan);
        PlanModification someOption = (PlanModification) options.get(0);
        PlanModification otherOption = (PlanModification) options.get(1);

        Plan nextPlan = (Plan) problem.apply(someOption, someInitialPlan);
        logger.info("AFTER : " + nextPlan);

        assertFalse(nextPlan.getFlaws().contains(someOption.getTargetFlaw()));
        assertTrue(nextPlan.getFlaws().contains(otherOption.getTargetFlaw()));
    }

    /**
     * A helper function to the situation preceding the final step
     * @param plan : the plan we are working on.
     * @return the situation preceding the final mock step
     */
    private static PopSituation getFinalSituation(Plan plan){
        Step finalStep = plan.getSteps()
                .stream()
                .filter(step -> step.getActionInstance().getName().equals("final"))
                .findFirst()
                .get();

        return plan.getTc().getPrecedingSituation(finalStep);
    }

    /**
     * A helper function to get the onTable proposition asserted by some step in the initial plan
     * where all three blocks are on the table
     * @return the proposition that some block (C in this case) is on the table
     */
    private static ContextualAtom getOnTableProposition(Plan plan,String blockName) {
        Step initialStep = plan.getSteps()
                .stream()
                .filter(step -> step.getActionInstance().getName().equals("initial"))
                .findFirst()
                .get();

        Atom onTableAtom = initialStep.getActionConsequences().getAtoms()
                .stream()
                .filter(atom -> atom.getPredicate().getName().equals("onTable"))
                .filter(atom -> atom.toString().equals("onTable(Block "+blockName+")"))
                .findFirst()
                .get();

        return new ContextualAtom(initialStep.getActionInstance().getContext(),
                onTableAtom);
    }
}
