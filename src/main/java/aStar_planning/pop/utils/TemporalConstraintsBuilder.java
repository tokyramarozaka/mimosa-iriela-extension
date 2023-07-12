package aStar_planning.pop.utils;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.Plan;
import constraints.PartialOrder;
import constraints.TemporalConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemporalConstraintsBuilder {
    /**
     * Provides the necessary temporal constraints to add a new step to insert into the plan, with
     * its wrapping steps (before and after the step). The added will be added before a given
     * situation.
     * @param newStep : the step to insert into the plan
     * @param newStepEntry : the situation preceding the new step
     * @param newStepExit : the situation following the new step
     * @param situation : the situation containing the flaw we want to resolve.
     * @return the temporal constraints needed to insert a step and its wrapping situations
     */
    public static TemporalConstraints insertNewStepBeforeSituation(
            Plan plan,
            Step newStep,
            PopSituation newStepEntry,
            PopSituation newStepExit,
            PopSituation situation
    ){
        List<PartialOrder> partialOrders = new ArrayList<>();

        // Adds the temporal constraints of the step and its entry and exit situations
        partialOrders.addAll(wrapStep(newStep, newStepEntry, newStepExit));

        // Adds the exit situation before the given situation
        partialOrders.addAll(TemporalConstraintsBuilder.placeBefore(
                newStepExit,
                situation)
        );

        // Put the initial situation before the new step's entry situation, to link it all together
        partialOrders.add(new PartialOrder(
                plan.getTc().getFollowingSituation(plan.getInitialStep()), newStepEntry
        ));

        return new TemporalConstraints(partialOrders);
    }

    public static TemporalConstraints insertNewStepBetweenTwoSituations(
        Plan plan,
        Step newStep,
        PopSituation newStepEntry,
        PopSituation newStepExit,
        PopSituation leftEdgeSituation,
        PopSituation rightEdgeSituation
    ){
        List<PartialOrder> partialOrders = new ArrayList<>();

        // Adds the temporal constraints of the step and its entry and exit situations
        partialOrders.addAll(wrapStep(newStep, newStepEntry, newStepExit));

        // Place the step after the left edge
        partialOrders.addAll(TemporalConstraintsBuilder.placeBefore(
                leftEdgeSituation,
                newStepEntry)
        );

        // Place the step before the right edge
        partialOrders.addAll(TemporalConstraintsBuilder.placeBefore(
                newStepExit,
                rightEdgeSituation)
        );

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

    public static List<PartialOrder> placeBefore(PopSituation toPlace, PopSituation situation) {
        return Arrays.asList(new PartialOrder(toPlace, situation));
    }
}
