package aStar_planning.pop.components;

import constraints.Codenotation;
import graph.Node;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import constraints.CodenotationConstraints;
import logic.Context;
import logic.ContextualAtom;
import logic.ContextualTerm;
import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Step implements PlanElement {
    private LogicalInstance actionInstance;
    private final static Logger logger = LogManager.getLogger(Step.class);

    /**
     * A shortcut to access the step's preconditions
     * @return the set of preconditions of the step
     */
    public ActionPrecondition getActionPreconditions(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getPreconditions();
    }

    /**
     * A shortcut to access the step's consequences.
     * @return the set of consequences of the step
     */
    public ActionConsequence getActionConsequences(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getConsequences();
    }

    /**
     * Checks if the current step makes the given proposition necessarily true in its preceding
     * situation. If true, we say that the current step ASSERTS the proposition
     * @param proposition : the proposition to check if it is asserted by the current step or not
     * @param cc : codenotations constraints describing variable bindings
     * @return true if the given proposition is asserted by the current step
     */
    public boolean asserts(ContextualAtom proposition,CodenotationConstraints cc){
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            CodenotationConstraints tempCc = cc.copy();
            ContextualAtom consequenceInstance = new ContextualAtom(
                    actionInstance.getContext(), consequence
            );

            if (proposition.getAtom().isNegation() == consequence.isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, tempCc))
            {
                        return true;
            }
        }

        return false;
    }

    /**
     * Determines if the current step destroys a given proposition without adding any other bindings
     * @param proposition : the proposition to check
     * @param cc : the current codenotation contraint of the plan.
     * @return true if this step destroys the given proposition and false otherwise.
     */
    public boolean destroys(ContextualAtom proposition, CodenotationConstraints cc){
        for (Atom consequence : this.getActionPreconditions().getAtoms()) {
            CodenotationConstraints tempCc = cc.copy();
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.actionInstance.getContext(), consequence
            );

            if (proposition.getAtom().isNegation() != consequenceInstance.getAtom().isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, tempCc)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if two predicates can be unified
     * @param consequenceInstance
     * @param proposition
     * @param tempCc
     * @return
     */
    private boolean canUnifyPropositions(
            ContextualAtom consequenceInstance,
            ContextualAtom proposition,
            CodenotationConstraints tempCc
    ){
        return consequenceInstance.getAtom().getPredicate().unify(
                        this.getActionInstance().getContext(),
                        proposition.getAtom().getPredicate(),
                        proposition.getContext(),
                        tempCc
                );
    }

    /**
     * Checks if the current step is threatening the precondition(s) of another step
     * TODO : make it work with codenotations constraints instead
     * @param step
     * @return
     */
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
                            step.getActionInstance().getContext()
                        )
                    )
                    .toList();

            if (destroyedPreconditions.size() > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current step destroys the precondition of another step, according to the
     * current bindings in the codenotation constraints
     * @param context
     * @param precondition
     * @param cc
     * @return
     */
    public boolean destroys(Context context, Atom precondition, CodenotationConstraints cc){
        ContextualAtom proposition = new ContextualAtom(context, precondition);

        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.getActionInstance().getContext(), consequence);

            if(consequence.isNegation() != proposition.getAtom().isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, cc)){
                return true;
            }
        }

        return false;
    }

    public CodenotationConstraints getAssertingCodenotations(ContextualAtom toAssert) {
        CodenotationConstraints assertingCodenotations = new CodenotationConstraints();

        for(Atom consequence : this.getActionConsequences().getAtoms()) {
            if (toAssert.getAtom().isNegation() == consequence.isNegation() &&
                    consequence.getPredicate().unify(
                            this.getActionInstance().getContext(),
                            toAssert.getAtom().getPredicate(),
                            toAssert.getContext(),
                            assertingCodenotations)
            ){
                return assertingCodenotations;
            } else {
                assertingCodenotations = new CodenotationConstraints();
            }
        }

        return assertingCodenotations;
    }

    public boolean isTheInitialStep(){
        return this.getActionInstance().getName().equals("initial");
    }

    public boolean isTheFinalStep(){
        return this.getActionInstance().getName().equals("final");
    }

    @Override
    public String toString() {
        return this.getActionInstance()
                .getLogicalEntity()
                .build(this.getActionInstance().getContext())
                .toString();
    }

    @Override
    public Node toNode() {
        return new Node(this.toString(), new ArrayList<>(), this);
    }

    public CodenotationConstraints toCodenotation(CodenotationConstraints toUpdate) {
        List<Codenotation> updates = new ArrayList<>(toUpdate.getCodenotations());

        this.getActionInstance().getContext().getContextPairs().forEach(pair -> {
            var left = new ContextualTerm(this.getActionInstance().getContext(),pair.getVariable());
            var right = pair.getContextualTerm();

            updates.add(new Codenotation(true, left, right));
        });

        return new CodenotationConstraints(updates);
    }
}
