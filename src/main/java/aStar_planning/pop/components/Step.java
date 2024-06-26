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
    private final static Logger logger = LogManager.getLogger(Step.class);
    private LogicalInstance actionInstance;

    /**
     * A shortcut to access the step's preconditions
     *
     * @return the set of preconditions of the step
     */
    public ActionPrecondition getActionPreconditions() {
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getPreconditions();
    }

    /**
     * A shortcut to access the step's consequences.
     *
     * @return the set of consequences of the step
     */
    public ActionConsequence getActionConsequences() {
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getConsequences();
    }

    /**
     * A shortcut to access the abstract action of this step
     *
     * @return Action that is instantiated by this step.
     */
    public Action getAction() {
        return (Action) this.actionInstance.getLogicalEntity();
    }

    /**
     * Checks if the current step makes the given proposition necessarily true in its preceding
     * situation. If true, we say that the current step ASSERTS the proposition.
     * This version does not change the given Codenotation Constraints.
     *
     * @param proposition : the proposition to check if it is asserted by the current step or not
     * @param cc          : codenotations constraints describing variable bindings which will NOT
     *                    be changed during the function as we use a copy of it.
     * @return true if the given proposition is asserted by the current step
     */
    public boolean asserts(ContextualAtom proposition, CodenotationConstraints cc) {
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            CodenotationConstraints tempCc = cc.copy();
            ContextualAtom consequenceInstance = new ContextualAtom(
                    actionInstance.getContext(), consequence
            );

            if (proposition.getAtom().isNegation() == consequence.isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, tempCc)
            ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the current step can assert the given proposition, and save the changes made to
     * codenotation constraints that would make the assertion possible.
     *
     * @param proposition : the proposition to check if it is asserted
     * @param cc          : the current codenotations set in the plan(which will change if need be)
     * @return
     */
    public boolean assertsWithPermanentCodenotations(
            ContextualAtom proposition,
            CodenotationConstraints cc
    ) {
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            ContextualAtom consequenceInstance = new ContextualAtom(
                    actionInstance.getContext(), consequence
            );

            if (proposition.getAtom().isNegation() == consequence.isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, cc)
            ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the current step destroys a given proposition without adding any other bindings
     *
     * @param proposition : the proposition to check
     * @param cc          : the current codenotation constraint of the plan.
     * @return true if this step destroys the given proposition and false otherwise.
     */
    public boolean destroys(ContextualAtom proposition, CodenotationConstraints cc) {
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            if (!consequence.getPredicate().getName().equals(
                    proposition.getAtom().getPredicate().getName())
            ) {
                continue;
            }

            CodenotationConstraints tempCc = cc.copy();
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.actionInstance.getContext(), consequence
            );

            if (proposition.getAtom().isNegation() != consequenceInstance.getAtom().isNegation() &&
                    canUnifyPropositions(consequenceInstance, proposition, tempCc)
            ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if two propositions (contextual atom) can be unified and commits to permanent
     * variable bindings (codenotation constraints)
     *
     * @param consequenceInstance : the first proposition to be unified
     * @param proposition         : the second proposition to be unified
     * @param tempCc              : current codenotation constraints representing the current bindings
     * @return true if the current codenotation constraints can unify the two propositions
     * or if they can unify it with some additional codenotation constraints
     */
    private boolean canUnifyPropositions(
            ContextualAtom consequenceInstance,
            ContextualAtom proposition,
            CodenotationConstraints tempCc
    ) {
        return consequenceInstance.getAtom().getPredicate().unify(
                consequenceInstance.getContext(),
                proposition.getAtom().getPredicate(),
                proposition.getContext(),
                tempCc
        );
    }


    /**
     * Returns the set of codenotation constraints which would make it so that this step asserts
     * the given proposition
     *
     * @param toAssert : the proposition to assert
     * @return a set of variable bindings (codenotation constraints) which would allow this step
     * to assert the given proposition, if any.
     */
    public CodenotationConstraints getAssertingCodenotations(ContextualAtom toAssert) {
        CodenotationConstraints assertingCodenotations = new CodenotationConstraints();

        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            if (toAssert.getAtom().isNegation() == consequence.isNegation() &&
                    consequence.getPredicate().unify(
                            this.getActionInstance().getContext(),
                            toAssert.getAtom().getPredicate(),
                            toAssert.getContext(),
                            assertingCodenotations)
            ) {
                return assertingCodenotations;
            } else {
                assertingCodenotations = new CodenotationConstraints();
            }
        }

        return assertingCodenotations;
    }

    public List<CodenotationConstraints> getAllAssertingCodenotations(
            ContextualAtom toAssert,
            CodenotationConstraints existingCc
    ) {
        List<CodenotationConstraints> allAssertingCodenotations = new ArrayList<>();

        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            CodenotationConstraints assertingCc = existingCc.copy();
            if (toAssert.getAtom().isNegation() == consequence.isNegation() &&
                    consequence.getPredicate().unify(
                            this.getActionInstance().getContext(),
                            toAssert.getAtom().getPredicate(),
                            toAssert.getContext(),
                            assertingCc)
            ) {
                CodenotationConstraints changes = assertingCc.differencesWith(existingCc);
                allAssertingCodenotations.add(changes);
            }
        }

        return allAssertingCodenotations;
    }

    /**
     * Checks if a step is the initial dummy step
     *
     * @return true if it is the initial step, and false otherwise
     */
    public boolean isTheInitialStep() {
        return this.getActionInstance().getName().equals("initial");
    }

    /**
     * Checks if a step is the final dummy step whose precondition is the completion of the goal
     *
     * @return true if it is the final step, and false otherwise
     */
    public boolean isTheFinalStep() {
        return this.getActionInstance().getName().equals("final");
    }


    @Override
    public Node toNode() {
        return new Node(this.toString(), new ArrayList<>(), this);
    }

    /**
     * Converts an action instance into codenotations based upon its context
     *
     * @return a set of codenotation constraints which copies the variable bindings in the context
     */
    public CodenotationConstraints toCodenotation() {
        List<Codenotation> updates = new ArrayList<>();

        this.getActionInstance().getContext().getContextPairs().forEach(pair -> {
            var left = new ContextualTerm(this.getActionInstance().getContext(), pair.getVariable());
            var right = pair.getContextualTerm();

            updates.add(new Codenotation(true, left, right));
        });

        return new CodenotationConstraints(updates);
    }

    @Override
    public String toString() {
        return this.getActionInstance().toString();
    }

    public String toStringWithCodenotations(CodenotationConstraints cc) {
        return this.getActionInstance().toStringWithCodenotations(cc);
    }
}
