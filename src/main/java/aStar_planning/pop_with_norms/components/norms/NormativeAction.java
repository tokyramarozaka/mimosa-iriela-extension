package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;

/**
 * A normative action is simple an action that can be the consequence of a norm
 */
public class NormativeAction extends Action implements NormConsequences{
    public NormativeAction(String name, ActionPrecondition preconditions, ActionConsequence consequences) {
        super(name, preconditions, consequences);
    }

    public NormativeAction(Action action){
        super(action.getName(), action.getPreconditions(), action.getConsequences());
    }

    @Override
    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        return false;
    }
}
