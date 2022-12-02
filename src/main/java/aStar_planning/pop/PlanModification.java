package aStar_planning.pop;

import aStar.Operator;
import logic.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@ToString
@Builder
public class PlanModification implements Operator {
    private List<PopSituation> addedSituations;
    private Step addedStep;
    private CodenotationConstraints addedCodenotationConstraints;
    private TemporalConstraints addedTemporalConstraints;

    public PlanModification(TemporalConstraints addedTemporalConstraints){
        this.addedTemporalConstraints = addedTemporalConstraints;
    }

    /**
     * Applies this plan modification to a given plan, and returns the resulting plan
     * @param oldPlan : the plan to be updated
     * @return the updated plan
     */
    public Plan apply(Plan oldPlan){
        List<PopSituation> allNewSituations = new ArrayList<>(oldPlan.getSituations());
        List<Step> allNewSteps = new ArrayList<>(oldPlan.getSteps());

        CodenotationConstraints allNewCodenotationConstraints = new CodenotationConstraints(
                new ArrayList<>(oldPlan.getCodenotationConstraints().getCodenotations()));
        TemporalConstraints allNewTemporalConstraints = new TemporalConstraints(
                oldPlan.getTemporalConstraints().getPartialOrders());

        if(addedSituations != null){
           allNewSituations.addAll(addedSituations);
        }
        if(addedStep != null){
            allNewSteps.add(addedStep);
        }
        if(addedCodenotationConstraints != null){
            allNewCodenotationConstraints.getCodenotations()
                    .addAll(this.addedCodenotationConstraints.getCodenotations());
        }
        if(addedTemporalConstraints != null){
            allNewTemporalConstraints.getPartialOrders()
                    .addAll(this.addedTemporalConstraints.getPartialOrders());
        }

        return new Plan(allNewSituations, allNewSteps, allNewCodenotationConstraints,
                allNewTemporalConstraints);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
