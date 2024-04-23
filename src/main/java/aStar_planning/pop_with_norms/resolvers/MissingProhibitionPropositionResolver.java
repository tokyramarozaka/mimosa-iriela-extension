package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.TemporalConstraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Operators to resolve a missing prohibition using promotion and demotion. Circumvention is also
 * an option.
 * @see aStar_planning.pop_with_norms.resolvers.CircumventionOperator for the circumvention operator
 */
public class MissingProhibitionPropositionResolver {
    /**
     * Resolves a forbidden proposition by promoting it so that it happens before the situation
     * where the prohibition is applicable
     * @param flaw
     * @param plan
     * @return
     */
    public List<Operator> byPromotion(NormativeFlaw flaw, NormativePlan plan) {
        List<Operator> operators = new ArrayList<>();
        PopSituation applicableSituation = flaw.getApplicableSituation();

        // TODO : complete these operators

        return operators;
    }
}
