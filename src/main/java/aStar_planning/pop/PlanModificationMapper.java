package aStar_planning.pop;

import logic.CodenotationConstraints;

import java.util.ArrayList;
import java.util.List;

public class PlanModificationMapper {
    public static PlanModification from(CodenotationConstraints codenotationConstraints){
        return new PlanModification()
                .builder()
                .addedCc(codenotationConstraints)
                .build();
    }

    public static PlanModification from(TemporalConstraints temporalConstraints){
        return new PlanModification()
                .builder()
                .addedTc(temporalConstraints)
                .build();
    }

    public static PlanModification insertStep(
            Step toInsert,
            Plan concernedPlan,
            PopSituation leftEdge,
            PopSituation rightEdge
    ){
        PopSituation entrySituation = new PopSituation();
        PopSituation exitSituation = new PopSituation();

        return new PlanModification()
                .builder()
                .addedStep(toInsert)
                .addedSituations(List.of(entrySituation,exitSituation))
                .addedTc(tcForStepInsertion(
                        toInsert,
                        leftEdge,
                        entrySituation,
                        exitSituation,
                        rightEdge
                ))
                .addedCc(toInsert.getAssertingCodenotations())
                .build();
    }

    private static TemporalConstraints tcForStepInsertion(
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
}
