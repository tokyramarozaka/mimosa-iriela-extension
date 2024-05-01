package aStar_planning.pop_with_norms.resolvers;

import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.PartialOrder;
import constraints.TemporalConstraints;
import logic.Action;
import logic.LogicalInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class to resolve mandatory actions within the plan.
 */
public class MissingProhibitionActionResolver {
  private final static Logger logger = LogManager.getLogger(
      MissingProhibitionActionResolver.class);

  public static List<PlanModification> byPromotion(NormativePlan plan, NormativeFlaw flaw) {
     if(flaw.getApplicableInterval().getBeginningSituation().equals(plan.getInitialSituation())){
          return new ArrayList<>();
     }

    Step targetStep = getStep(flaw);

    Optional<Step> forbiddenStep = getForbiddenStepFromPlan(plan, flaw, targetStep);

    PartialOrder stepBeforeSituation = new PartialOrder(
        plan.getTc().getFollowingSituation(forbiddenStep.get()),
        flaw.getApplicableInterval().getBeginningSituation());

    TemporalConstraints temporalChanges = new TemporalConstraints(new ArrayList<>(
        List.of(stepBeforeSituation)));

    return new ArrayList<>(List.of(PlanModificationMapper.from(flaw, temporalChanges)));
  }

  public static List<PlanModification> byDemotion(NormativePlan plan, NormativeFlaw flaw) {
    PopSituation inapplicableSituation = flaw.getApplicableInterval().getEndingSituation();

    if (inapplicableSituation == null || plan.getFinalSituation().equals(inapplicableSituation)) {
      return new ArrayList<>();
    }

    Step targetStep = getStep(flaw);

    Optional<Step> forbiddenStep = getForbiddenStepFromPlan(plan, flaw, targetStep);

    PartialOrder stepAfterInapplicableSituation = new PartialOrder(
        inapplicableSituation, plan.getTc().getPrecedingSituation(forbiddenStep.get()));
    TemporalConstraints temporalChanges = new TemporalConstraints(new ArrayList<>(
        List.of(stepAfterInapplicableSituation)));

    return new ArrayList<>(List.of(PlanModificationMapper.from(flaw, temporalChanges)));
  }

  private static Optional<Step> getForbiddenStepFromPlan(
      NormativePlan plan,
      NormativeFlaw flaw,
      Step targetStep) {

    Optional<Step> forbiddenStep = plan.getSteps().stream()
        .filter(step -> step.equals(targetStep))
        .findFirst();

    if (forbiddenStep.isEmpty()) {
      throw new RuntimeException("Flaw's prohibited step was not detected during resolution");
    }
    return forbiddenStep;
  }

  private static Step getStep(NormativeFlaw flaw) {
    Action forbiddenAction = (Action) flaw.getFlawedNorm().getNormConsequences();
    return new Step(new LogicalInstance(
        forbiddenAction,
        flaw.getContext()));
  }
}
