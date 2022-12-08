package aStar_planning.pop.mapper;

import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.TemporalConstraints;
import aStar_planning.pop.utils.TemporalConstraintsBuilder;
import logic.CodenotationConstraints;

import java.util.List;

/**
 * A class to map plan components to plan modifications. This is useful when building a plan
 * modification with either of the following parameters :
 * <ul>
 *     <li>A set of codenotation constraints : by changing the variable bindings</li>
 *     <li>A set of temporal constraints : by arranging the partial order of the plan</li>
 *     <li>A set of step, wrapped by its entry situations, binded by its codenotations, and
 *     arranged by some temporal constraints.</li>
 * </ul>
 * Please note that no modifications on either situations, steps, cc, or tc is represented by null.
 */
public class PlanModificationMapper {
    /**
     * Builds a plan modification based on (non)codenotation constraints.
     * @param codenotationConstraints : the codenotations constraints to add.
     * @return a plan modification encapsulating the added codenotations.
     */
    public static PlanModification from(CodenotationConstraints codenotationConstraints){
        return PlanModification
                .builder()
                .addedCc(codenotationConstraints)
                .build();
    }

    /**
     * Builds a plan modification based on a set of temporal constraints.
     * @param temporalConstraints : the temporal constraints we want to add.
     * @return a plan modification encapsulating the added temporal constraints
     */
    public static PlanModification from(TemporalConstraints temporalConstraints){
        return PlanModification
                .builder()
                .addedTc(temporalConstraints)
                .build();
    }

    /**
     * Builds a plan modification by adding a new step, its wrapping situations (before and after),
     * new bindings through codenotations, and their partial-order in the plan.
     * @param situations : the wrapping situations of the added step : at the entry and the exit.
     * @param step : the added step
     * @param cc : the added codenotation constraints, mainly those of the new step.
     * @param tc : the added temporal constraints to update the partial order of the plan
     * @return a plan modification encapsulating the changes from a new step in the plan.
     */
    public static PlanModification from(List<PopSituation> situations, Step step,
                                        CodenotationConstraints cc, TemporalConstraints tc)
    {
        return PlanModification
                .builder()
                .addedSituations(situations)
                .addedStep(step)
                .addedCc(cc)
                .addedTc(tc)
                .build();
    }

}
