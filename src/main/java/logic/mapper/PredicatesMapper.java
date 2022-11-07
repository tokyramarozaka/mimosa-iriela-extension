package logic.mapper;

import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Predicate;
import logic.Situation;

import java.util.ArrayList;
import java.util.List;

public class PredicatesMapper {
    public static List<ContextualAtom> toContextualAtoms(List<Predicate> predicates) {
        List<ContextualAtom> contextualAtoms = new ArrayList<>();

        predicates.forEach(predicate -> {
            contextualAtoms.add(
                    new ContextualAtom(new Context(),new Atom(false, predicate))
            );
        });

        return contextualAtoms;
    }
}
