package logic.mappers;

import logic.Context;
import logic.ContextualPredicate;
import logic.Situation;

import java.util.ArrayList;
import java.util.List;

public class SituationMapper {
    /**
     * Converts an external situation into an internal situation, meaning that we add technical
     * details like the notion of context to the user's definition of a situation.
     * @param toConvert : the external situation to convert.
     * @return an internal situation, which has a set of predicates and their contexts.
     */
    public static Situation convertToInternal(planning.Situation toConvert){
        List<ContextualPredicate> contextualPredicateList = new ArrayList<>();

        toConvert.getPropositions().forEach(proposition -> {
            contextualPredicateList.add(new ContextualPredicate(new Context(), proposition));
        });

        return new Situation(contextualPredicateList);
    }
}
