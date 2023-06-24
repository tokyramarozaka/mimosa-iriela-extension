package aStar_planning.pop_with_norms.resolvers;

import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;

import java.util.List;

/**
 * Operators to resolve a missing prohibition using promotion and demotion. Circumvention is also
 * an option.
 * @see aStar_planning.pop_with_norms.resolvers.CircumventionOperator for the circumvention operator
 */
public class MissingProhibitionPropositionResolver {
    public static List<PlanModification> byPromotion(NormativePlan plan, NormativeFlaw flaw){
        // TODO
        return null;
    }

    public static List<PlanModification> byDemotion(NormativePlan plan, NormativeFlaw flaw){
        // TODO
        return null;
    }
}
