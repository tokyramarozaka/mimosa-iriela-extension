package logic;

import aStar.State;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Situation implements State {
    private List<ContextualPredicate> contextualPredicates;
    private Context stateContext = new Context();

    public Situation(List<ContextualPredicate> contextualPredicates) {
        this.contextualPredicates = contextualPredicates;
    }

    public List<LogicalInstance> allPossibleActionInstances(List<Action> possibleActions) {
        List<LogicalInstance> allActionInstances = new ArrayList<>();

        possibleActions.forEach(action -> {
            allActionInstances.addAll(action.possibleInstances(this));
        });

        return allActionInstances;
    }

    /**
     * TODO : refactor the code so that it is more comprehensible
     *
     * @param actionInstance
     * @return
     */
    public Situation applyActionInstance(LogicalInstance actionInstance) {
        List<ContextualPredicate> toAdd = new ArrayList<>();
        Action action = (Action) actionInstance.getLogicalEntity();

        // What to add from the set of consequences ?
        for (Atom atom : action.getConsequences().getAtoms()) {
            boolean negates = false;
            // To all beliefs, Is there a belief negated by the current consequence ?
            for (ContextualPredicate belief : this.getContextualPredicates()) {
                // If yes, it is not going to be added to the new State
                if (atom.getPredicate().unify(actionInstance.getContext(), belief.getPredicate(),
                        belief.getContext()) && (atom.isNegation())
                ) {
                    negates = true;
                }
            }
            // If no belief was negated by it, then the atom is a new predicate to Add into the new State
            if (!negates) {
                toAdd.add(new ContextualPredicate(actionInstance.getContext(), atom.getPredicate()));
            }
        }

        // What to keep from the current belief ?
        for (ContextualPredicate belief : this.getContextualPredicates()) {
            boolean wasNegated = false;
            for (Atom consequence : action.getConsequences().getAtoms()) {
                if (consequence.getPredicate().unify(
                        actionInstance.getContext(),belief.getPredicate(),belief.getContext())
                        && (consequence.isNegation())
                ){
                    wasNegated = true;
                }
            }
            if (!wasNegated) {
                toAdd.add(new ContextualPredicate(
                        new Context(), (Predicate) belief.getPredicate().buildConsequence(belief.getContext())));
            }
        }

        List<ContextualPredicate> resultingWorld = new ArrayList<>(toAdd);
        return new Situation(resultingWorld);
    }

    /**
     * Checks if a situation satisfies a given goal
     * @param goal: the goal to attain with its propositions
     * @return true if the goal is satisfied in the given situation and false otherwise
     */
    public boolean satisfies(Goal goal) {
        Context stateContext = new Context();
        int accomplished = 0;

        for (Atom goalProposition : goal.getGoalPropositions()) {
            for (ContextualPredicate belief : this.getContextualPredicates()) {
                if (belief.getPredicate().unify(
                        belief.getContext(), goalProposition.getPredicate()
                                .buildConsequence(goal.getGoalContext()), stateContext
                )) {
                    accomplished++;
                    break;
                }
            }
        }

        return accomplished == goal.getGoalPropositions().size();
    }

    public double goalDistance(Goal goal) {
        double distance = 0;

        return (double) goal.getGoalPropositions()
                .stream()
                .filter(proposition -> !new ContextualAtom(goal.getGoalContext(), proposition)
                        .isVerified(this))
                .count();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger i = new AtomicInteger();

        stringBuilder.append("Situation(");

        this.contextualPredicates.forEach(contextualPredicate -> {
            stringBuilder.append(
                    contextualPredicate.getPredicate().buildConsequence(contextualPredicate.getContext())
            );
            if (i.getAndIncrement() < this.contextualPredicates.size() - 1) {
                stringBuilder.append(" , ");
            }
        });

        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
