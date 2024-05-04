package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import constraints.PartialOrder;
import constraints.TemporalConstraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Operators to resolve a missing prohibition using promotion and demotion. Circumvention is also
 * an option.
 *
 * @see aStar_planning.pop_with_norms.resolvers.CircumventionOperator for the circumvention operator
 */
public class MissingProhibitionPropositionResolver {
    /**
     * Resolves a forbidden proposition by promoting it so that it happens before the situation
     * where the prohibition is applicable
     *
     * @param flaw: the normative flaw to be resolved
     * @param plan: the normative plan containing the flaw
     * @return a list of Operators (=plan modification) which resolves the `flaw` in the `plan`
     */
    public static List<Operator> byPromotion(NormativePlan plan, NormativeFlaw flaw) {
        List<Operator> operators = new ArrayList<>();
        PopSituation applicableSituation = flaw.getApplicableSituation();
        NormativeProposition forbiddenProposition = (NormativeProposition) flaw
                .getFlawedNorm()
                .getNormConsequences();

        Step establisher = plan.getEstablisher(forbiddenProposition, flaw.getApplicableSituation());

        if (establisher == null) {
            throw new NullPointerException("Establisher not found");
        }
        PopSituation establisherPostSituation = plan.getTc().getFollowingSituation(establisher);

        TemporalConstraints toAdd = new TemporalConstraints(List.of(
                new PartialOrder(establisherPostSituation, applicableSituation)
        ));

        operators.add(PlanModificationMapper.from(flaw, toAdd));

        return operators;
    }

    /**
     * Resolves a present prohibited proposition by demoting the establisher of the forbidden
     * proposition towards the end of the applicable interval.
     *
     * @param plan
     * @param flaw
     * @return
     */
    public static List<Operator> byDemotion(NormativePlan plan, NormativeFlaw flaw) {
        List<Operator> operators = new ArrayList<>();
        PopSituation applicableSituation = flaw.getApplicableSituation();
        PopSituation inapplicableSituation = plan
                .getInapplicableSituationAfter(applicableSituation, flaw.getFlawedNorm());

        // If the prohibition is always applicable a demotion is not possible...
        if (inapplicableSituation == null) {
            return new ArrayList<>();
        }
        // If the prohibition ceases to be applicable in the final situation, no demotion possible
        // (Nothing can be put after the final situation... because it's the final situation.)
        if (inapplicableSituation.equals(plan.getFinalSituation())){
            return new ArrayList<>();
        }

        NormativeProposition forbiddenProposition = (NormativeProposition) flaw.getFlawedNorm()
                .getNormConsequences();

        Step establisher = plan.getEstablisher(forbiddenProposition, flaw.getApplicableSituation());

        if (establisher == null) {
            throw new NullPointerException("Establisher not found");
        }

        // If it is established by the initial step then it cannot be demoted
        if (establisher.isTheInitialStep()) {
            return new ArrayList<>();
        }

        PopSituation establisherPreviousSituation = plan.getTc().getPrecedingSituation(establisher);

        TemporalConstraints toAdd = new TemporalConstraints(List.of(
                new PartialOrder(inapplicableSituation, establisherPreviousSituation)
        ));

        operators.add(PlanModificationMapper.from(flaw, toAdd));

        return operators;
    }

}
