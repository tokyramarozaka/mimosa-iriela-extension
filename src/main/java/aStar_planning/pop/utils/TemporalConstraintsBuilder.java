package aStar_planning.pop.utils;

import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.Plan;
import constraints.PartialOrder;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.TemporalConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemporalConstraintsBuilder {
    /**
     * Provides the necessary temporal constraints to add a new step to insert into the plan, with
     * its wrapping steps (before and after the step). The added step must be added before the
     * new step.
     * @param newStep : the step to insert into the plan
     * @param newStepEntry : the situation preceding the new step
     * @param newStepExit : the situation following the new step
     * @param flawedSituation : the situation containing the flaw we want to resolve.
     * @return the temporal constraints needed to insert a step and its wrapping situations
     */
    public static TemporalConstraints insertNewStepBetween(
            Plan plan,
            Step newStep,
            PopSituation newStepEntry,
            PopSituation newStepExit,
            PopSituation flawedSituation
    ){
        List<PartialOrder> partialOrders = new ArrayList<>();

        // Adds the temporal constraints of the step and its entry and exit situations
        partialOrders.addAll(wrapStep(newStep, newStepEntry, newStepExit));

        // Adds the exit situation before the following step
        partialOrders.addAll(TemporalConstraintsBuilder.placeBefore(
                newStepExit,
                flawedSituation)
        );

        // Put the entry situation before the new step's entry situation, to link it all together
        partialOrders.add(new PartialOrder(
                plan.getTc().getFollowingSituation(plan.getInitialStep()), newStepEntry
        ));

        return new TemporalConstraints(partialOrders);
    }

    /**
     * Wraps a step with a situation preceding the step, and another following it.
     * @param toWrap : the step to wrap situations around;
     * @param entry : the situation before the step
     * @param exit : the situation after the step
     * @return the temporal orders describing that both situations are placed before and after
     * the step.
     */
    private static List<PartialOrder> wrapStep(Step toWrap, PopSituation entry, PopSituation exit){
        return Arrays.asList(
                new PartialOrder(entry, toWrap),
                new PartialOrder(toWrap, exit)
        );
    }

    public static TemporalConstraints tcForStepInsertion(
            Step addedStep,
            PopSituation leftEdge,
            PopSituation entrySituation,
            PopSituation exitSituation,
            PopSituation rightEdge
    ){
        List<PartialOrder> toAdd = new ArrayList<>();

        toAdd.add(new PartialOrder(leftEdge, entrySituation));
        toAdd.add(new PartialOrder(entrySituation, addedStep));
        toAdd.add(new PartialOrder(addedStep, exitSituation));
        toAdd.add(new PartialOrder(exitSituation, rightEdge));

        return new TemporalConstraints(toAdd);
    }

    public static List<PartialOrder> placeBefore(PopSituation toPlace, PopSituation situation) {
        return Arrays.asList(new PartialOrder(toPlace, situation));
    }
}
