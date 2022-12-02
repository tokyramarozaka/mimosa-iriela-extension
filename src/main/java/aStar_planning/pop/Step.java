package aStar_planning.pop;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.ContextualAtom;
import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Step implements PlanElement {
    private LogicalInstance actionInstance;

    public ActionPrecondition getActionPreconditions(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getPreconditions();
    }

    public ActionConsequence getActionConsequences(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getConsequences();
    }
    public boolean asserts(ContextualAtom proposition,
                           CodenotationConstraints codenotationConstraints)
    {
        for (Atom consequence : this.getActionPreconditions().getAtoms()) {
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.getActionInstance().getContext(), consequence);

            if(assertsProposition(consequenceInstance, proposition, codenotationConstraints)){
                return true;
            }
        }

        return false;
    }

    private boolean assertsProposition(ContextualAtom proposition, ContextualAtom otherProposition,
                                       CodenotationConstraints codenotationConstraints){
        CodenotationConstraints temp = codenotationConstraints.copy();

        return proposition.getAtom().isNegation() == otherProposition.getAtom().isNegation()
                && otherProposition.getAtom().getPredicate()
                    .unify(
                            this.getActionInstance().getContext(),
                            proposition.getAtom().getPredicate(),
                            proposition.getContext()
                    );
    }

    public boolean isThreatening(Step step) {
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            List<Atom> destroyedPreconditions = step.getActionPreconditions().getAtoms()
                    .stream()
                    .filter(precondition -> precondition.getPredicate()
                            .sameName(consequence.getPredicate()))
                    .filter(precondition -> precondition.isNegation() != consequence.isNegation()
                        && consequence.getPredicate().unify(
                            this.getActionInstance().getContext(),
                            precondition.getPredicate(),
                            step.getActionInstance().getContext())
                    )
                    .toList();

            if (destroyedPreconditions.size() > 0){
                return true;
            }
        }
        return false;
    }

}