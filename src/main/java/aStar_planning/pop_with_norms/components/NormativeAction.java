package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.concepts.ActionName;
import constraints.CodenotationConstraints;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A normative action is simple an action that can be the consequence of a norm
 */
public class NormativeAction extends Action implements NormConsequences {
    private final static Logger logger = LogManager.getLogger(NormativeAction.class);

    public NormativeAction(
            ActionName actionName,
            ActionPrecondition preconditions,
            ActionConsequence consequences
    ) {
        super(actionName, preconditions, consequences);
    }

    /**
     * Builds a normative action based on an already existing action
     *
     * @param action : the action we want to build a normative action upon
     */
    public NormativeAction(Action action) {
        super(action.getName(), action.getPreconditions(), action.getConsequences());
    }

    @Override
    public boolean isApplied(
            OrganizationalPlan plan,
            PopSituation situation,
            CodenotationConstraints cc
    ) {
        PlanElement followingElement = plan.getTc().getFollowingElement(situation);

        if (followingElement == null) {
            logger.info("following element is null");
            return false;
        } else if (followingElement instanceof Step followingStep) {
            if (!this.getName().equals(followingStep.getActionInstance().getName())) {
                return false;
            }

            Context stateContext = new Context();
            for (Atom normConsequence : this.getConsequences().getAtoms()) {
                for (Atom consequence : followingStep.getActionConsequences().getAtoms()) {
                    if (!normConsequence.getPredicate().unify(
                            stateContext,
                            consequence.getPredicate(),
                            followingStep.getActionInstance().getContext(),
                            cc
                    )) {
                        logger.info(this.getName() + " is applied is FALSE because : " +
                                normConsequence + " and " + consequence + " does not match.");
                        return false;
                    }
                }
            }

            return true;
        } else if (followingElement instanceof PopSituation) {
            return isApplied(plan, (PopSituation) followingElement, cc);
        }

        return false;
    }

    public NormativeAction build(Context context) {
        return new NormativeAction((Action) super.build(context));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
