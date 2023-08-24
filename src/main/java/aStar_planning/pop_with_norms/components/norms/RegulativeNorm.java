package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import exception.UnapplicableNormException;
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
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;
    private final static Logger logger = LogManager.getLogger(RegulativeNorm.class);

    /**
     * Determines whether the regulative norm enforces some action that ought (not) to be done.
     * @return true if the norm enforces some action, and false if it enforces some proposition
     * instead
     */
    public boolean enforceAction(){
        return this.getNormConsequences() instanceof NormativeAction;
    }

    /**
     * Determines whether the regulative norm enforces some proposition that ought (not) to be done.
     * @return true if the norm enforces some proposition, and false if it enforces some action
     * instead
     */
    public boolean enforceProposition(){
        return this.getNormConsequences() instanceof NormativeProposition;
    }

    /**
     * Checks if a given normative action is satisfied by a given step. To do this, we must
     * first check the variable bindings that make the norm applicable using its applicability
     * conditions, then we must see if the norm's consequences are carried out by the given step
     * or not.
     * Keep in mind that the algorithm may vary depending on its deontic operator : OBLIGATION,
     * PROHIBITION, PERMISSION.
     * @return true if the given step actually satisfies the normative action, false otherwise.
     */
    public boolean isSatisfiedIn(NormativePlan plan, PopSituation situation){
        plan.removeRedundantTemporalConstraints();
        try {
            CodenotationConstraints applicableCodenotations = this.getNormConditions()
                         .getApplicableCodenotations(plan, situation);

            return this.normConsequences.isApplied(plan, situation, applicableCodenotations);
        }catch(UnapplicableNormException e){
            return false;
        }
    }

    /**
     * Return the codenotation constraints that would make this norm applicable in a given situation
     * (if any, if there is none, then an exception is thrown).
     * @param plan : the plan within which we want to check for applicable codenotations
     * @param situation : the situation in the plan where we want to check for applicability
     *                  conditions
     * @return the set of codenotations that makes the current norm applicable
     * @throws UnapplicableNormException if there is no codenotation that would make the norm
     * applicable.
     */
    public CodenotationConstraints getApplicableCodenotations (
            NormativePlan plan,
            PopSituation situation
    ) throws UnapplicableNormException{
        return this.getNormConditions().getApplicableCodenotations(plan, situation);
    }

    /**
     * Checks if the norm is being applied in a given situation of a given plan.
     * @param plan : the plan to check
     * @param situation : the situation we want to verify it in
     * @return true if the norm is applied, and false otherwise.
     */
    @Override
    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        List<PlanElement> allFollowingElements = plan.getSteps().stream()
                .filter(step -> plan.getTc().isBefore(situation, step))
                .collect(Collectors.toList());

        for (PlanElement followingElement : allFollowingElements) {
            if(followingElement instanceof Step){
                if(this.isSatisfiedIn(plan, situation)){
                    if(this.isProhibition()){
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        }
        return isProhibition();
    }

    private boolean isProhibition() {
        return this.deonticOperator.equals(DeonticOperator.PROHIBITION);
    }

    private boolean isObligation() {
        return this.deonticOperator.equals(DeonticOperator.OBLIGATION);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(")
                .append(this.deonticOperator)
                .append(")")
                .append(" : ")
                .append(this.normConditions)
                .append(" -> ")
                .append(this.normConsequences)
                .toString();
    }

    @Override
    public LogicalEntity build(Context context) {
        return new RegulativeNorm(
                this.deonticOperator,
                this.normConditions.build(context),
                this.normConsequences.build(context)
        );
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
