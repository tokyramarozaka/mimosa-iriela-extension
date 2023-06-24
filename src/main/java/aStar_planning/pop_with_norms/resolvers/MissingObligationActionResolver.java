package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop.utils.TemporalConstraintsBuilder;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
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
        PopSituation currentSituation = flaw.getSituation();

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
        Action actionToAdd = (Action) flaw.getFlawedNorm().getNormConsequences();
        LogicalInstance addedMandatoryAction = new LogicalInstance(actionToAdd, new Context());

        // todo : unify and change the context accordingly.
        CodenotationConstraints applicableCodenotations = flaw.getFlawedNorm()
                .getApplicableCodenotations(plan, flaw.getSituation());

        Step newStep = new Step(addedMandatoryAction);
        PopSituation newStepEntry = new PopSituation();
        PopSituation newStepExit = new PopSituation();
        TemporalConstraints tcChanges = TemporalConstraintsBuilder.insertNewStepBetween(
                plan, newStep, newStepEntry, newStepExit, flaw.getSituation()
        );

        return List.of(
                PlanModificationMapper.from(
                        flaw,
                        List.of(newStepEntry, newStepExit),
                        newStep,
                        applicableCodenotations,
                        tcChanges
                )
        );
    }
}