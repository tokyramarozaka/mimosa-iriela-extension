package logic.mappers;

import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Goal;
import logic.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PredicatesMapper {
    public static List<ContextualAtom> toGoalContextualAtoms(List<Predicate> predicates) {
        List<ContextualAtom> contextualAtoms = new ArrayList<>();

        predicates.forEach(predicate -> {
            contextualAtoms.add(
                    new ContextualAtom(new Context(),new Atom(false, predicate))
            );
        });

        return contextualAtoms;
    }

    public static Goal toGoal(List<Predicate> predicates){
        return new Goal(
                new Context(),
                predicates
                        .stream()
                        .map(predicate -> new Atom(false,predicate))
                        .collect(Collectors.toList()));
    }
}
