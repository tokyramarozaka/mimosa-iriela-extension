package aStar_planning.pop_with_norms.resolvers;

import aStar.Operator;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.resolvers.OpenConditionResolver;
import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import constraints.CodenotationConstraints;
import logic.Action;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CircumventionOperator {
    private static final Logger logger = LogManager.getLogger(CircumventionOperator.class);
    public static List<Operator> circumvent(
            NormativePlan plan,
            NormativeFlaw flaw,
            List<Action> possibleActions
    ) {
        List<Operator> operators = new ArrayList<>();

        flaw.getFlawedNorm().getNormConditions().getConditions().forEach(condition -> {
            OpenCondition toSolve = createReverseOpenCondition(
                    plan, flaw, condition, flaw.getContext()
            );
            operators.addAll(plan.resolve(toSolve, possibleActions));
        });

        return operators;
    }

    /**
     * Returns a plan with codenotations that makes the flaw applicable. This in turn is to find
     * out what codenotation do we need to work with to solve the flaw.
     * @param plan : the initial normative plan where the flaw has been found.
     * @param flaw : the normative flaw to solve
     * @return a temporary plan where the condition is being codenotated to some values
     */
    private static NormativePlan getApplicablePlan(NormativePlan plan, NormativeFlaw flaw) {
        CodenotationConstraints applicableCodenotations = flaw.getFlawedNorm()
                .getApplicableCodenotations(plan, flaw.getApplicableSituation(), flaw.getContext())
                .fuseWith(plan.getCc());

        return new NormativePlan(
                plan.getSituations(),
                plan.getSteps(),
                applicableCodenotations,
                plan.getTc(),
                plan.getOrganizations()
        );
    }

    /**
     * Creates an open condition on the negation of some condition of some norm.
     * @param plan : the plan in which the normative flaw was found.
     * @param flaw : the normative flaw
     * @param condition : the condition to circumvent
     * @return an OpenCondition based on the negation of the condition.
     */
    private static OpenCondition createReverseOpenCondition(
            NormativePlan plan,
            NormativeFlaw flaw,
            Atom condition,
            Context conditionContext
    ){
        ContextualAtom toAssert = new ContextualAtom(
                conditionContext,
                new Atom(!condition.isNegation(), condition.getPredicate())
        );

        PopSituation openConditionSituation = getOpenConditionSituation(plan, flaw);

        return new OpenCondition(openConditionSituation, toAssert);
    }

    private static PopSituation getOpenConditionSituation(
            NormativePlan plan,
            NormativeFlaw flaw
    ) {
        if(flaw.getFlawedNorm().enforceProposition() &&
                flaw.getFlawedNorm().getDeonticOperator().equals(DeonticOperator.PROHIBITION)
        ){
            NormativeProposition forbiddenProposition = (NormativeProposition) flaw
                    .getFlawedNorm()
                    .getNormConsequences();

            Step establisher = plan.getEstablisher(forbiddenProposition, plan.getFinalSituation());

            if(!establisher.isTheInitialStep()){
                return plan.getTc().getPrecedingSituation(establisher);
            } else {
                return plan.getInitialSituation();
            }
        } else {
            return flaw.getApplicableSituation();
        }
    }
}
