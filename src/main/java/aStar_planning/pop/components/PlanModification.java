package aStar_planning.pop.components;

import aStar.Operator;
import constraints.CodenotationConstraints;
import constraints.PartialOrder;
import constraints.TemporalConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PlanModification implements Operator {
    private Flaw targetFlaw;
    private List<PopSituation> addedSituations;
    private Step addedStep;
    private CodenotationConstraints addedCc;
    private TemporalConstraints addedTc;

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
                new ArrayList<>(oldPlan.getTc().getPartialOrders()));

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        return builder
                .append("Plan Modification to resolve : "+this.getTargetFlaw())
                .append(addedStep == null ? "" : "\nAdded : \n\t"+this.addedStep)
                .append(addedSituations == null ? "" : "\nAdded : \n\t"+this.addedSituations)
                .append(addedCc == null ? "" : "\nAdded : \n\t"+this.addedCc)
                .append(addedTc == null ? "" : "\nAdded : \n\t"+this.addedTc)
                .toString();
    }
}
