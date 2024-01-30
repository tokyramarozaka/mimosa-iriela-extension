package logic;

import aStar.Operator;
import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Constraint extends Graphic implements State {
    private List<ContextualAtom> contextualAtoms;

    public boolean isApplicable(Situation situation){
        for (ContextualAtom contextualAtom : this.contextualAtoms) {
            if (!contextualAtom.isVerified(situation)){
                return false;
            }
        }
        return true;
    }

    public boolean isCoherent(List<Rule> constraintRules){
        for (Rule constraintRule : constraintRules) {
            if (constraintRule.isApplicable(this)){
                if(!constraintRule.isApplied(this)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isVerified(Situation toTest){
        for (ContextualAtom constraintContextualAtom : this.contextualAtoms) {
            if(!constraintContextualAtom.isVerified(toTest)){
                return false;
            }
        }

        return true;
    }

    public List<Operator> getContributingInstances(List<Action> possibleActions){
        List<Operator> contributingInstances = new ArrayList<>();

        for (ContextualAtom constraintAtom : this.getContextualAtoms()) {
            for (Action possibleAction : possibleActions) {
                //contributingInstances.addAll(possibleAction.getProducingInstances(constraintAtom));
            }
        }

        return contributingInstances;
    }

    public Constraint revertAction(LogicalInstance actionToRevert) {
        Constraint previousConstraint = this.copy();

        previousConstraint.getContextualAtoms().removeAll(this.addedConsequences(actionToRevert));
        previousConstraint.getContextualAtoms().addAll(this.addedPreconditions(actionToRevert));

        return previousConstraint;
    }

    private List<ContextualAtom> addedConsequences(LogicalInstance instance) {
        List<ContextualAtom> toDel = new ArrayList<ContextualAtom>();
        Action action = (Action) instance.getLogicalEntity();
        Context actionContext = instance.getContext().copy();

        for (ContextualAtom belief : this.getContextualAtoms()) {
            for (Atom consequence : action.getConsequences().getAtoms()) {
                if (!consequence.isNegation() && consequence.getPredicate().unify(
                        actionContext,belief.getAtom().getPredicate(), belief.getContext()
                )){
                    toDel.add(belief);
                }
            }
        }

        return toDel;
    }

    private List<ContextualAtom> addedPreconditions(LogicalInstance instance){
        Action action = (Action) instance.getLogicalEntity();
        List<ContextualAtom> preconditionsAtoms = new ArrayList<ContextualAtom>();

        for (Atom precondition : action.getPreconditions().getAtoms()) {
            preconditionsAtoms.add(new ContextualAtom(instance.getContext(), precondition));
        }

        return preconditionsAtoms;
    }

    public Constraint copy(){
        return new Constraint(this.contextualAtoms.stream().toList());
    }

    public double evaluateCompletion(Situation initialSituation) {
        double uncompletedGoals = this.contextualAtoms.size();

        for (ContextualAtom contextualAtom : this.getContextualAtoms()) {
            if(contextualAtom.isVerified(initialSituation)){
                uncompletedGoals--;
            }
        }

        return uncompletedGoals;
    }

    @Override
    public String toGraphNode() {
        return this.toString();
    }
}
