package pop_IT.mock;

import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.utils.PlanInitializer;
import constraints.Codenotation;
import constraints.CodenotationConstraints;
import constraints.PartialOrder;
import constraints.TemporalConstraints;
import logic.Context;
import logic.ContextualTerm;
import logic.Goal;
import logic.LogicalInstance;
import logic.Situation;
import logic.Variable;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A generator for mock plans for unit tests on Partial-Order Planning. Allows abstraction
 * on the tedious process of building some sample plans.
 */
public class MockPlan {
    private final static Logger logger = LogManager.getLogger(MockPlan.class);

    private static List<Step> someSteps_StackAndDrop(){
        List<Step> steps = new ArrayList<>();

        Step stackInstance = new Step(new LogicalInstance(
                ActionFactory.allActionsInBlocksWorld().get(0),
                new Context()
        ));
        Step dropInstance = new Step(new LogicalInstance(
                ActionFactory.allActionsInBlocksWorld().get(1),
                new Context()
        ));

        steps.add(stackInstance);
        steps.add(dropInstance);

        return steps;
    }

    private static List<PopSituation> someSituations(){
        PopSituation situation1 = new PopSituation();
        PopSituation situation2 = new PopSituation();
        List<PopSituation> someSituations = new ArrayList<>(Arrays.asList(situation1, situation2));

        return someSituations;
    }

    private static CodenotationConstraints invalidCodenotationConstraint__byContradiction(
            ContextualTerm contextualTerm1,
            ContextualTerm contextualTerm2
    ){
        List<Codenotation> invalidCodenotations = new ArrayList<>();

        invalidCodenotations.add(
                new Codenotation(true, contextualTerm1, contextualTerm2));
        invalidCodenotations.add(
                new Codenotation(false, contextualTerm1, contextualTerm2));

        return new CodenotationConstraints(invalidCodenotations);
    }

    public static Plan planWithInvalidCc__hasContradiction(){
        ContextualTerm contextualTerm1 = new ContextualTerm(new Context(), new Variable("x"));
        ContextualTerm contextualTerm2 = new ContextualTerm(new Context(), new Variable("y"));
        
        return Plan.builder()
                .situations(new ArrayList<>())
                .steps(new ArrayList<>())
                .cc(invalidCodenotationConstraint__byContradiction(contextualTerm1, contextualTerm2))
                .tc(new TemporalConstraints())
                .build();
    }

    public static Plan planWithInvalidTc(){
        List<PopSituation> situations = someSituations();

        return Plan.builder()
                .situations(situations)
                .steps(new ArrayList<>())
                .cc(new CodenotationConstraints())
                .tc(someInvalidTemporalConstraints(situations.get(0), situations.get(1)))
                .build();
    }

    private static TemporalConstraints someInvalidTemporalConstraints(PlanElement element1,
                                                                      PlanElement element2){
        List<PartialOrder> partialOrders = new ArrayList<>();

        partialOrders.add(new PartialOrder(element1, element2));
        partialOrders.add(new PartialOrder(element2, element1));

        return new TemporalConstraints(partialOrders);
    }


    public static Plan plan_withThreeFreeBlocks_to_stackedBlocks() {
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocks_ABC_stacked();

        return PlanInitializer.constructInitialPlan(initialSituation, goal);
    }
}
