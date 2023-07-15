package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop.utils.TemporalConstraintsBuilder;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import constraints.PartialOrder;
import constraints.TemporalConstraints;
import logic.Action;
import logic.Context;
import logic.LogicalInstance;

import java.util.ArrayList;
import java.util.List;

public class MissingObligationActionResolver {
    public static List<PlanModification> byPromotion(NormativePlan plan, NormativeFlaw flaw) {
        List<PlanModification> operators = new ArrayList<>();
        Action mandatoryAction = (Action) flaw.getFlawedNorm().getNormConsequences();
        PopSituation currentSituation = flaw.getApplicableSituation();

        plan.getSteps().stream()
                .filter(step -> step.getActionInstance().getLogicalEntity().getLabel()
                        .equals(mandatoryAction.getLabel()))
                .forEach(toPromote -> {
                    PopSituation situationPostEstablisher = plan.getTc()
                            .getFollowingSituation(toPromote);

                    PartialOrder temporalChange = new PartialOrder(
                            situationPostEstablisher,
                            currentSituation
                    );

                    List<PartialOrder> newPartialOrders = plan.getTc().getPartialOrders();
                    newPartialOrders.add(temporalChange);
                    TemporalConstraints tcChanges = new TemporalConstraints(newPartialOrders);

                    if (tcChanges.isCoherent()) {
                        operators.add(PlanModificationMapper.from(
                                flaw,
                                new TemporalConstraints(List.of(temporalChange)))
                        );
                    }
                });

        return operators;
    }

    public static List<Operator> byCreation(NormativePlan plan, NormativeFlaw flaw) {
        // if the flaw happens in the final situation no steps can be added after it
        if (flaw.getApplicableSituation().equals(plan.getFinalSituation())) {
            return new ArrayList<>();
        }

        Action actionToAdd = (Action) flaw.getFlawedNorm().getNormConsequences();
        LogicalInstance addedMandatoryAction = new LogicalInstance(actionToAdd, new Context());

        // Builds the step and its wrapping situations
        Step newStep = new Step(addedMandatoryAction);
        PopSituation newStepEntry = new PopSituation();
        PopSituation newStepExit = new PopSituation();

        // Adds the step and its wrapping situations in the plan's temporal constraints
        PopSituation situationAfterNewStep = (PopSituation) plan.getTc()
                .getFollowingElement(flaw.getApplicableSituation());
        TemporalConstraints addedTemporalConstraints = TemporalConstraintsBuilder
                .insertNewStepBetweenTwoSituations(
                        plan, newStep, newStepEntry, newStepExit,
                        flaw.getApplicableSituation(), situationAfterNewStep
                );

        // Adds the necessary codenotation constraints to the plan for the new step
        CodenotationConstraints applicableCodenotations = flaw.getFlawedNorm()
                .getApplicableCodenotations(plan, flaw.getApplicableSituation());

        return List.of(PlanModificationMapper.from(
                        flaw,
                        List.of(newStepEntry, newStepExit),
                        newStep,
                        applicableCodenotations,
                        addedTemporalConstraints
                )
        );
    }
}