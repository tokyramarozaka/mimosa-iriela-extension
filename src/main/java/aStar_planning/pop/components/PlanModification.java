package aStar_planning.pop.components;

import aStar.Operator;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class PlanModification implements Operator {
    private final static Logger logger = LogManager.getLogger(PlanModification.class);
    private Flaw targetFlaw;
    private List<PopSituation> addedSituations;
    private Step addedStep;
    private CodenotationConstraints addedCc;
    private TemporalConstraints addedTc;

    /**
     * Applies this plan modification to a given plan, and returns the resulting plan
     *
     * @param oldPlan : the plan to be updated
     * @return the updated plan
     */
    public Plan apply(Plan oldPlan) {
        List<PopSituation> allNewSituations = new ArrayList<>(oldPlan.getSituations());
        List<Step> allNewSteps = new ArrayList<>(oldPlan.getSteps());

        CodenotationConstraints allNewCodenotationConstraints = new CodenotationConstraints(
                new ArrayList<>(oldPlan.getCc().getCodenotations()));
        TemporalConstraints allNewTemporalConstraints = new TemporalConstraints(
                new ArrayList<>(oldPlan.getTc().getPartialOrders()));

        if (addedSituations != null) {
            allNewSituations.addAll(addedSituations);
        }
        if (addedStep != null) {
            allNewSteps.add(addedStep);
        }
        if (addedCc != null) {
            allNewCodenotationConstraints.getCodenotations()
                    .addAll(this.addedCc.getCodenotations());
        }
        if (addedTc != null) {
            this.addedTc.getPartialOrders().forEach(partialOrder -> {
                if (allNewTemporalConstraints.getPartialOrders()
                        .stream()
                        .noneMatch(existing -> existing.toString().equals(partialOrder.toString()))
                ) {
                    allNewTemporalConstraints.getPartialOrders().add(partialOrder);
                }
            });
        }

        return new Plan(allNewSituations, allNewSteps, allNewCodenotationConstraints,
                allNewTemporalConstraints);
    }

    public NormativePlan apply(NormativePlan oldPlan) {
        List<PopSituation> allNewSituations = new ArrayList<>(oldPlan.getSituations());
        List<Step> allNewSteps = new ArrayList<>(oldPlan.getSteps());

        CodenotationConstraints allNewCodenotationConstraints = new CodenotationConstraints(
                new ArrayList<>(oldPlan.getCc().getCodenotations()));
        TemporalConstraints allNewTemporalConstraints = new TemporalConstraints(
                new ArrayList<>(oldPlan.getTc().getPartialOrders()));

        if (addedSituations != null) {
            allNewSituations.addAll(addedSituations);
        }
        if (addedStep != null) {
            allNewSteps.add(addedStep);
        }
        if (addedCc != null) {
            allNewCodenotationConstraints.getCodenotations()
                    .addAll(this.addedCc.getCodenotations());
        }
        if (addedTc != null) {
            this.addedTc.getPartialOrders().forEach(partialOrder -> {
                if (allNewTemporalConstraints.getPartialOrders()
                        .stream()
                        .noneMatch(existing -> existing.toString().equals(partialOrder.toString()))
                ) {
                    allNewTemporalConstraints.getPartialOrders().add(partialOrder);
                }
            });
        }

        return new NormativePlan(
                allNewSituations,
                allNewSteps,
                allNewCodenotationConstraints,
                allNewTemporalConstraints,
                oldPlan.getOrganizations()
        );
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Plan Modification to resolve : ")
                .append(this.getTargetFlaw())
                .append(addedStep == null ? "" : "\nAdded : \n\t" + this.addedStep)
                .append(addedSituations == null ? "" : "\nAdded : \n\t" + this.addedSituations)
                .append(addedCc == null ? "" : "\nAdded : \n\t" + this.addedCc)
                .append(addedTc == null ? "" : "\nAdded : \n\t" + this.addedTc)
                .toString();
    }

    @Override
    public String toGraphArc() {
        return new StringBuilder()
                .append(addedStep == null ? "" : "+" + this.addedStep + "\n")
                .append(addedSituations == null ? "" : "+" + this.addedSituations + "\n\n")
                .append(addedCc == null ? "" : "+" + this.addedCc.toSimpleString() + "\n\n")
                .append(addedTc == null ? "" : "+" + this.addedTc.toSimpleString() + "\n")
                .toString();
    }
}
