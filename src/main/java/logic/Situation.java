package logic;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Situation implements State {
    private List<ContextualPredicate> contextualPredicates;
    private Context stateContext = new Context();

    public Situation(List<ContextualPredicate> contextualPredicates){
        this.contextualPredicates = contextualPredicates;
    }

    public List<LogicalInstance> allPossibleActionInstances(List<Action> possibleActions){
        List<LogicalInstance> allActionInstances = new ArrayList<>();

        possibleActions.forEach(action -> {
            allActionInstances.addAll(action.possibleInstances(this));
        });

        return allActionInstances;
    }

    public Situation applyActionInstance(LogicalInstance actionInstance){
        List<ContextualPredicate> nextSituationPredicates = new ArrayList<>();
        Action action = (Action) actionInstance.getLogicalEntity();

        action.getPreconditions().getAtoms().forEach(consequence -> {

        });

        return new Situation(nextSituationPredicates);
    }

    public boolean satisfies(Goal goal) {
        Context stateContext = new Context();
        int accomplished = 0;

        for(ContextualAtom goalProposition : goal.getPropositions()) {
            for(ContextualPredicate belief : this.getContextualPredicates()) {
                if(belief.getPredicate().unify(
                        belief.getContext(), goalProposition.getAtom().getPredicate()
                        .build(goalProposition.getContext()),stateContext
                )){
                    accomplished++;
                    break;
                }
            }
        }

        return accomplished == goal.getPropositions().size();
    }

    public double goalDistance(Goal goal) {
        double distance = 0;

        return (double) goal.getPropositions()
                .stream()
                .filter(proposition -> !proposition.isVerified(this))
                .count();
    }
}
