package aStar_planning.pop.components;

import aStar.Operator;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class PlanModification implements Operator {
    private List<PopSituation> addedSituations;
    private Step addedStep;
    private CodenotationConstraints addedCc;
    private TemporalConstraints addedTc;

    public PlanModification(TemporalConstraints addedTc){
        this.addedTc = addedTc;
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
                new ArrayList<>(oldPlan.getCc().getCodenotations()));
        TemporalConstraints allNewTemporalConstraints = new TemporalConstraints(
                oldPlan.getTc().getPartialOrders());

        if(addedSituations != null){
           allNewSituations.addAll(addedSituations);
        }
        if(addedStep != null){
            allNewSteps.add(addedStep);
        }
        if(addedCc != null){
            allNewCodenotationConstraints.getCodenotations()
                    .addAll(this.addedCc.getCodenotations());
        }
        if(addedTc != null){
            allNewTemporalConstraints.getPartialOrders()
                    .addAll(this.addedTc.getPartialOrders());
        }

        return new Plan(allNewSituations, allNewSteps, allNewCodenotationConstraints,
                allNewTemporalConstraints);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
