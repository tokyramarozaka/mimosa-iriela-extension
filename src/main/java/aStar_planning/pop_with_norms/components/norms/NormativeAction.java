package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Context;

/**
 * A normative action is simple an action that can be the consequence of a norm
 */
public class NormativeAction extends Action implements NormConsequences{
    public NormativeAction(
            String name,
            ActionPrecondition preconditions,
            ActionConsequence consequences
    ){
        super(name, preconditions, consequences);
    }

    /**
     * Builds a normative action based on an already existing action
     * @param action : the action we want to build a normative action upon
     */
    public NormativeAction(Action action){
        super(action.getName(), action.getPreconditions(), action.getConsequences());
    }

    @Override
    public boolean isApplied(
            NormativePlan plan,
            PopSituation situation,
            CodenotationConstraints cc
    ){  
        PlanElement followingElement = plan.getTc().getFollowingElement(situation);

        if(followingElement == null){
            return false;
        } else if(followingElement instanceof Step){
            Context stateContext = new Context();
            Step followingStep = (Step) followingElement;

            for (Atom normConsequence : this.getConsequences().getAtoms()) {
                for (Atom consequence : followingStep.getActionConsequences().getAtoms()){
                    if(!normConsequence.getPredicate().unify(
                            stateContext,
                            consequence.getPredicate(),
                            followingStep.getActionInstance().getContext(),
                            cc
                    )){
                        return false;
                    }
                }
            }
        }else if(followingElement instanceof PopSituation){
            return isApplied(plan, (PopSituation) followingElement, cc);
        }

        return false;
    }

    @Override
    public NormativeAction build(Context context){
        return new NormativeAction((Action)super.build(context));
    }
}
