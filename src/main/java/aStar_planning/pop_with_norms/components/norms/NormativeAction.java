package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.components.NormativePlan;
import constraints.CodenotationConstraints;
import logic.Action;
import logic.Atom;
import logic.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A normative action is simple an action that can be the consequence of a norm
 */
public class NormativeStep implements NormConsequences{
    private Step step;
    private final static Logger logger = LogManager.getLogger(NormativeStep.class);

    public NormativeStep(Step step){
        this.step = step;
    }

    @Override
    public boolean isApplied(
            NormativePlan plan,
            PopSituation situation,
            CodenotationConstraints cc
    ){
        PlanElement followingElement = plan.getTc().getFollowingElement(situation);
        Action stepAction = (Action) this.step.getActionInstance().getLogicalEntity();
        Context stepContext =  this.step.getActionInstance().getContext();

        if(followingElement == null){
            return false;
        } else if(followingElement instanceof Step followingStep){
            if(!this.step.getActionInstance().getName()
                    .equals(followingStep.getActionInstance().getName()))
            {
                return false;
            }

            for (Atom normConsequence : stepAction.getConsequences().getAtoms()) {
                for (Atom consequence : followingStep.getActionConsequences().getAtoms()){
                    if(!normConsequence.getPredicate().unify(
                            stepContext,
                            consequence.getPredicate(),
                            followingStep.getActionInstance().getContext(),
                            cc
                    )){
//                        logger.info(this.getName() + " is applied is FALSE because : " +
//                                normConsequence + " and " + consequence + " does not match.");
                        return false;
                    }
                }
            }
//            logger.info(this.getName() + " is applied is true in " + situation);
            return true;
        }else if(followingElement instanceof PopSituation){
            return isApplied(plan, (PopSituation) followingElement, cc);
        }

        return false;
    }

    @Override
    public NormativeStep build(Context context){
        return new NormativeStep((Action)super.build(context));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
