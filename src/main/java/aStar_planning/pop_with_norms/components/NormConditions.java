package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import exception.UnapplicableNormException;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NormConditions {
    private List<Atom> conditions;
    private final static Logger logger = LogManager.getLogger(NormConditions.class);

    public NormConditions build(Context context) {
        return new NormConditions(this.conditions.stream()
                .map(atom -> atom.build(context))
                .collect(Collectors.toList()));
    }


    /**
     * Returns all the codenotation constraints that would make the current norm conditions to be
     * applicable, if any.
     * @param plan : the current plan containing the norm
     * @param situation : the situation where we want to check out the applicability conditions
     * @return a set of codenotation constraints that makes the norm applicable.
     */
    public CodenotationConstraints getApplicableCodenotations(
            NormativePlan plan,
            PopSituation situation
    )throws UnapplicableNormException{
        CodenotationConstraints cc = new CodenotationConstraints();
        Context conditionContext = new Context();

        for (Atom condition : this.conditions) {
            Predicate conditionPredicate = condition.getPredicate();
            boolean isUnifiedOnce = false;

            for(ContextualAtom assertedProposition : plan.getAllAssertedPropositions(situation)){
                if(conditionPredicate.unify(
                        conditionContext,
                        assertedProposition.getAtom().getPredicate(),
                        assertedProposition.getContext(),
                        cc
                )){
//                    logger.info("\tOK for : "+condition+" for " + situation+"\n");
                    isUnifiedOnce = true;
                    break;
                }
            }

            if(!isUnifiedOnce){
//                logger.info("\tNO APPLICABLE CODENOTATIONS\n");
                throw new UnapplicableNormException(this, situation);
            }
        }

        return cc;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int conditionsCount = 0;
        for (Atom condition : conditions) {
            stringBuilder.append(condition);
            if (conditionsCount++ < this.conditions.size() - 1) {
                stringBuilder.append(" , ");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
