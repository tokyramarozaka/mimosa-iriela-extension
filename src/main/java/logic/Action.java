package logic;

import aStar_planning.pop_with_norms.concepts.ActionName;
import constraints.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * An action representation based on the STRIPS paradigm. It needs a :
 * <ul>
 *     <li>name (a string) : to easily describe the action</li>
 *     <li>preconditions (a set of atoms) : under which conditions is the action executable ? </li>
 *     <li>consequences (a set of atoms) : the produced / removed atoms after its execution </li>
 * </ul>
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class Action extends LogicalEntity {
    private final Logger logger = LogManager.getLogger(Action.class);
    private ActionName actionName;
    private ActionPrecondition preconditions;
    private ActionConsequence consequences;

    /**
     * Returns all the possible instances of a given action in a target situation based on its
     * preconditions
     *
     * @param targetSituation : the situation to check if any instance of the action is possible.
     * @return a list of action instances, executable in the target situation
     */
    public List<LogicalInstance> possibleInstances(Situation targetSituation) {
        List<Context> contexts = new ArrayList<>();
        contexts.add(new Context());

        /*
         * Gets all possible contexts and store it inside the contexts variable
         * The increasing function here is : f(n) n being the index of the condition,
         * and its superior born is the precondition's size
         */
        possibleInstancesRecursive(this.preconditions.getAtoms(), targetSituation, contexts);

        return contexts
                .stream()
                .map(context -> new LogicalInstance(this, context))
                .toList();
    }

    /**
     * Recursive algorithms to get all contexts from each branch, the recursion stops when we have
     * treated all the precondition's predicates
     *
     * @param precondition
     * @param beliefs
     * @param contexts
     */
    public void possibleInstancesRecursive(
            List<Atom> precondition,
            Situation beliefs,
            List<Context> contexts
    ) {
        Context stateContext = new Context();

        for (Atom condition : precondition) {
            List<Context> contextsToAdd = new ArrayList<>();
            List<Context> contextsToDel = new ArrayList<>();

            for (Context context : contexts) {
                boolean unified_once = false;
                Context oldContext = context.copy();

                for (ContextualPredicate belief : beliefs.getContextualPredicates()) {
                    if (condition.getPredicate().unify(
                            context, belief.getPredicate().build(belief.getContext()), stateContext
                    )) {
                        if (unified_once) {
                            contextsToAdd.add(context);
                        } else {
                            unified_once = true;
                        }
                        context = oldContext.copy();
                    }
                }
                if (!unified_once) {
                    contextsToDel.add(context);
                }
            }

            contexts.addAll(contextsToAdd);
            contexts.removeAll(contextsToDel);
        }
    }

    @Override
    public LogicalEntity build(Context context) {
        return new Action(this.actionName, this.preconditions.build(context),
                this.consequences.build(context));
    }

    public LogicalEntity build(Context context, CodenotationConstraints cc){
        return new Action(this.actionName, this.preconditions.build(context, cc),
                this.consequences.build(context, cc));
    }

    @Override
    public LogicalEntity copy() {
        return new Action(this.actionName, this.preconditions.copy(), this.consequences.copy());
    }

    @Override
    public String getLabel() {
        return this.actionName.getName();
    }

    @Override
    public String toString() {
        int i = 0;
        List<Term> terms = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(this.getActionName());
        sb.append("(");

        for (Atom p : (this.getPreconditions().getAtoms())) {
            p.getPredicate().getTerms().forEach(term -> {
                if (!terms.contains(term))
                    terms.add(term);
            });
        }

        for (Term term : terms) {
            sb.append(term);
            if (i++ < terms.size() - 1)
                sb.append(",");
        }

        sb.append(")");
        return sb.toString();
    }

    public boolean unify(Context context, Action to, Context toContext, CodenotationConstraints cc) {
        List<ContextualTerm> changes = new ArrayList<>();

        boolean res = attemptUnification(context, to, toContext, changes, cc);
        if (!res) {
            for (ContextualTerm linkedVariable : changes) {
                cc.unlink(linkedVariable);
            }
        }

        return res;
    }

    private boolean attemptUnification(
            Context context,
            Action to,
            Context toContext,
            List<ContextualTerm> changes,
            CodenotationConstraints cc
    ) {
        if (!this.getActionName().getName().equals(to.getActionName().getName())) {
            return false;
        }

        ActionPrecondition fromPrecondition = this.getPreconditions();
        ActionPrecondition toPrecondition = to.getPreconditions();
        for (int index = 0; index < fromPrecondition.getAtoms().size(); index++) {
            Predicate fromPredicate = fromPrecondition.getAtoms().get(index).getPredicate();
            Predicate toPredicate = toPrecondition.getAtoms().get(index).getPredicate();
            if (!fromPredicate.attemptUnification(
                    context,
                    toPredicate,
                    toContext,
                    changes,
                    cc
            )) {
                return false;
            }
        }

        return true;
    }

    public List<Term> getTerms() {
        List<Term> terms = new ArrayList<>();

        this.getPreconditions().getAtoms().forEach(atom -> {
            for (Term term : atom.getPredicate().getTerms()) {
                if(!terms.contains(term)){
                    terms.add(term);
                }
            }
        });

        this.getConsequences().getAtoms().forEach(atom -> {
            for (Term term : atom.getPredicate().getTerms()) {
                if(!terms.contains(term)){
                    terms.add(term);
                }
            }
        });

        return terms;
    }
}
