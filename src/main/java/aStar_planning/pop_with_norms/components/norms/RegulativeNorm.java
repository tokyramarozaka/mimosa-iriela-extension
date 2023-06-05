package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import logic.Atom;
import logic.Context;
import logic.LogicalEntity;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class RegulativeNorm extends Norm {
    private String name;
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;

    public boolean isApplicable(NormativePlan plan, PopSituation situation){
        return this.normConditions.isApplicable(plan, situation);
    }
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
     * TODO : checks if a given normative action is satisfied by a given step. To do this, we must
     * first check the variable bindings that make the norm applicable using its applicability
     * conditions, then we must see if the norm's consequences are carried out by the given step
     * or not.
     * Keep in mind that the algorithm may vary depending on its deontic operator : OBLIGATION,
     * PROHIBITION, PERMISSION.
     * @return true if the given step actually satisfies the normative action, false otherwise.
     */
    public boolean isSatisfiedBy(NormativePlan plan, PopSituation situation){
        try {
            CodenotationConstraints applicableCodenotations = this.getNormConditions()
                         .getApplicableCodenotations(plan, situation);

            return this.normConsequences.isApplied(plan, situation, applicableCodenotations);
        }catch(NullPointerException e){
            return false;
        }
    }

    /**
     * Checks if the norm is being applied in a given situation of a given plan.
     * @param plan : the plan to check
     * @param situation : the situation we want to verify it in
     * @return true if the norm is applied, and false otherwise.
     */
    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        PlanElement followingElement = plan.getTc().getFollowingElement(situation);

        if(followingElement == null){
            return false;
        } else if(followingElement instanceof Step){
            return this.isSatisfiedBy(plan, situation);
        }else if(followingElement instanceof PopSituation){
            return isApplied(plan, (PopSituation) followingElement);
        }

        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.name)
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
                this.name,
                this.deonticOperator,
                this.normConditions.build(context),
                this.normConsequences.build(context)
        );
    }

    @Override
    public LogicalEntity copy() {
        return new RegulativeNorm(
                this.name,
                this.deonticOperator,
                new NormConditions(new ArrayList<>(this.normConditions.getConditions())),
                this.getNormConsequences()
        );
    }

    @Override
    public String getLabel() {
        return this.name;
    }


}
