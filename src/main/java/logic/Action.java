package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Action extends LogicalEntity{
    private String name;
    private ActionPrecondition preconditions;
    private ActionConsequence consequences;

    public List<LogicalInstance> possibleInstances(Situation targetSituation){
        List<Context> contexts = new ArrayList<>();

        contexts.add(new Context());

        /*
         * Gets all possible contexts and store it inside the contexts variable
         * The increasing function here is : f(n) n being the index of the condition,
         * and its superior born is the precondition's size
         */
        possibleInstancesRecursive(this.preconditions.getAtoms(),targetSituation, contexts);

        return contexts
                .stream()
                .map(context -> new LogicalInstance(this, context))
                .toList();
    }

    /**
     * Recursive algorithms to get all contexts from each branch,
     * the recursion stops when we have treated all the precondition's predicates
     * @param precondition
     * @param beliefs
     * @param contexts
     */
    public void possibleInstancesRecursive(List<Atom> precondition, Situation beliefs,
                                     List<Context> contexts) {
        Context stateContext = new Context();

        for(Atom condition : precondition) {
            List<Context> contextsToAdd = new ArrayList<>();
            List<Context> contextsToDel = new ArrayList<>();

            for(Context context : contexts) {
                boolean unified_once = false;
                Context oldContext = context.copy();

                for(ContextualPredicate belief : beliefs.getContextualPredicates()) {
                    if(condition.getPredicate().unify(
                            context, belief.getPredicate().build(belief.getContext()), stateContext
                    )){
                        if(unified_once){
                            contextsToAdd.add(context);
                        } else {
                            unified_once = true;
                        }
                        context = oldContext.copy();
                    }
                }
                if (unified_once == false) {
                    contextsToDel.add(context);
                }
            }

            contexts.addAll(contextsToAdd);
            contexts.removeAll(contextsToDel);
        }
    }

    @Override
    public LogicalEntity build(Context context) {
        return new Action(this.name, this.preconditions.build(context),
                this.consequences.build(context));
    }

    @Override
    public LogicalEntity copy() {
        return new Action(this.name, this.preconditions.copy(), this.consequences.copy());
    }

}