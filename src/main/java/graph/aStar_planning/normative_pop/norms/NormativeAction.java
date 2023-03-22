package graph.aStar_planning.normative_pop.norms;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;

public class NormativeAction extends Action implements NormConsequences{
    public NormativeAction(String name, ActionPrecondition preconditions, ActionConsequence consequences) {
        super(name, preconditions, consequences);
    }
}
