package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import exception.NoPlanFoundException;
import exception.UnapplicableNormException;
import logic.Atom;
import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class RegulativeNorm extends Norm {
    private final static Logger logger = LogManager.getLogger(RegulativeNorm.class);
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;

    /**
     * Determines whether the regulative norm enforces some action that ought (not) to be done.
     *
     * @return true if the norm enforces some action, and false if it enforces some proposition
     * instead
     */
    public boolean enforceAction() {
        return this.getNormConsequences() instanceof NormativeAction;
    }

    /**
     * Determines whether the regulative norm enforces some proposition that ought (not) to be done.
     *
     * @return true if the norm enforces some proposition, and false if it enforces some action
     * instead
     */
    public boolean enforceProposition() {
        return this.getNormConsequences() instanceof NormativeProposition;
    }

    /**
     * Return the codenotation constraints that would make this norm applicable in a given situation
     * (if any, if there is none, then an exception is thrown).
     *
     * @param plan      : the plan within which we want to check for applicable codenotations
     * @param situation : the situation in the plan where we want to check for applicability
     *                  conditions
     * @return the set of codenotations that makes the current norm applicable
     * @throws UnapplicableNormException if there is no codenotation that would make the norm
     *                                   applicable.
     */
    public CodenotationConstraints getApplicableCodenotations(
            NormativePlan plan,
            PopSituation situation,
            Context context
    ) throws UnapplicableNormException {
        return this.getNormConditions().getApplicableCodenotations(plan, situation, context);
    }

    /**
     * Checks if the norm is being applied in a given situation of a given plan.
     * @param plan      : the plan to check
     * @param situation : the situation we want to verify it in
     * @return true if the norm is applied, and false otherwise.
     */
    public boolean isApplied(
            NormativePlan plan,
            PopSituation situation,
            CodenotationConstraints cc,
            Context applicableContext
    ) {
//        if (this.enforceAction()) {
//            // TODO : ask JP about it...
//            return checkForActionAfterwards(plan, situation);
//        }
        return false;
    }

    public boolean applicableWithin(PopSituation situation, Interval situationInterval) {
//        TODO : check if the situation where this norm is applicable is within a certain interval
//        try{
//            return situationInterval.isWithin(plan, situation);
//        }catch(NoPlanFoundException e){
//
//        }
        return false;
    }

    /**
     * Checks if an obligation is already within the goal of the agent. When it is, it is no longer
     * a normative flaw, but it will become a goal which the agent shall achieve.
     * @param plan
     * @param proposition
     * @param applicableContext
     * @return
     */
    private boolean isAlreadyInGoals(
            NormativePlan plan,
            NormativeProposition proposition,
            Context applicableContext
    ) {
        Step finalStep = plan.getFinalStep();
        logger.debug("Checking if " + proposition + " is in : " + finalStep);

        for (Atom precondition : finalStep.getActionPreconditions().getAtoms()) {
            if (precondition.getPredicate().unify(
                    finalStep.getActionInstance().getContext(),
                    proposition.getPredicate(),
                    applicableContext,
                    plan.getCc()
            ) && (precondition.isNegation() == proposition.isNegation())) {
                return true;
            }
        }
        return false;
    }

    public boolean isProhibition() {
        return this.deonticOperator.equals(DeonticOperator.PROHIBITION);
    }

    public boolean isObligation() {
        return this.deonticOperator.equals(DeonticOperator.OBLIGATION);
    }

    public boolean isPermission() {
        return this.deonticOperator.equals(DeonticOperator.PERMISSION);
    }

    @Override
    public String toString() {
        return "(" +
                this.deonticOperator +
                ")" +
                " : " +
                this.normConditions +
                " -> " +
                this.normConsequences;
    }

    @Override
    public LogicalEntity build(Context context) {
        return new RegulativeNorm(
                this.deonticOperator,
                this.normConditions.build(context),
                this.getNormConsequences()
        );
    }

    @Override
    public LogicalEntity build(Context context, CodenotationConstraints cc) {
        // TODO : represent a norm with all their bindings for visual purposes
        return null;
    }

    @Override
    public LogicalEntity copy() {
        return new RegulativeNorm(
                this.deonticOperator,
                new NormConditions(new ArrayList<>(this.normConditions.getConditions())),
                this.getNormConsequences()
        );
    }

    @Override
    public String getLabel() {
        return this.toString();
    }
}
