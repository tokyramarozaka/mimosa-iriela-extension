package logic.mappers;

import logic.Context;
import logic.ContextualPredicate;
import logic.Situation;

import java.util.ArrayList;
import java.util.List;

public class SituationMapper {
    public static Situation convertToInternal(planning.Situation toConvert){
        List<ContextualPredicate> contextualPredicateList = new ArrayList<>();

        toConvert.getPropositions().forEach(proposition -> {
            contextualPredicateList.add(new ContextualPredicate(new Context(), proposition));
        });

        return new Situation(contextualPredicateList);
    }
}
