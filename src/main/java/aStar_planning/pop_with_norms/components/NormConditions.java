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
    private final static Logger logger = LogManager.getLogger(NormConditions.class);
    private List<Atom> conditions;

    /**
     * Returns all the codenotation constraints that would make the current norm conditions to be
     * applicable, if any.
     *
     * @param plan      : the current plan containing the norm
     * @param situation : the situation where we want to check out the applicability conditions
     * @return a set of codenotation constraints that makes the norm applicable.
     */
    public CodenotationConstraints getApplicableCodenotations(
            NormativePlan plan,
            PopSituation situation,
            Context conditionContext
    ) throws UnapplicableNormException {
        CodenotationConstraints cc = plan.getCc().copy();

        for (Atom condition : this.conditions) {
            if (plan.isRole(condition)) {
                if(!plan.validatedByConstitutiveNorms(condition, conditionContext, cc)){
//                    logger.error(condition + " failed in situation : " + situation + " | " + cc);
                    throw new UnapplicableNormException(this, situation);
                }
            } else {
                Predicate conditionPredicate = condition.getPredicate();
                boolean isUnifiedOnce = false;

                for(ContextualAtom assertedProposition : plan.getAllAssertedPropositions(situation)){
                    if (conditionPredicate.unify(
                            conditionContext,
                            assertedProposition.getAtom().getPredicate(),
                            assertedProposition.getContext(),
                            cc
                    )) {
                        isUnifiedOnce = true;
                        break;
                    }
                }

                if (!isUnifiedOnce && !condition.isNegation()) {
//                    logger.error(condition + " failed in situation : " + situation + " | " + cc);
                    throw new UnapplicableNormException(this, situation);
                } else if (isUnifiedOnce && condition.isNegation()){
//                    logger.error(condition + " failed in situation : " + situation + " | " + cc);
                    throw new UnapplicableNormException(this, situation);
                }
            }
//            logger.trace(condition + " is OK : " + cc);
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

    public NormConditions build(Context context) {
        return new NormConditions(this.conditions.stream()
                .map(atom -> atom.build(context))
                .collect(Collectors.toList()));
    }
}
