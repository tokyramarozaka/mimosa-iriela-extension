package aStar_planning.pop_with_norms.resolvers;

import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop_with_norms.components.NormativeStepFlaw;
import constraints.PartialOrder;
import constraints.TemporalConstraints;

import java.util.ArrayList;
import java.util.List;

public class MissingProhibitionActionResolver {
    public static List<PlanModification> byPromotion(NormativeStepFlaw flaw){
        Step forbiddenStep = flaw.getStep();

        PartialOrder stepBeforeSituation = new PartialOrder(
                forbiddenStep,
                flaw.getApplicableSituation()
        );
        TemporalConstraints temporalChanges = new TemporalConstraints(new ArrayList<>(
                List.of(stepBeforeSituation))
        );

        return new ArrayList<>(List.of(PlanModificationMapper.from(flaw, temporalChanges)));
    }

    public static List<PlanModification> byDemotion(NormativeStepFlaw flaw){
        Step forbiddenStep = flaw.getStep();
        
        PartialOrder stepAfterSituation = new PartialOrder(
                forbiddenStep,
                flaw.getApplicableSituation()
        );
        TemporalConstraints temporalChanges = new TemporalConstraints(new ArrayList<>(
                List.of(stepAfterSituation))
        );

        return new ArrayList<>(List.of(PlanModificationMapper.from(flaw, temporalChanges)));
    }
}
