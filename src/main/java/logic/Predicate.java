package logic;

import constraints.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Predicate implements Unifiable {
    private String name;
    private List<Term> terms;

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext) {
        List<ContextualTerm> changes = new ArrayList<>();

        Boolean unifyAttemptSuccess = attemptUnification(fromContext, to, toContext, changes);

        if (!unifyAttemptSuccess) {
            for (ContextualTerm change : changes) {
                change.getContext().unlink((Variable) change.getTerm());
            }
        }

        return unifyAttemptSuccess;
    }

    /**
     * Attemps an unification using the codenotations constraints instead of the context
     *
     * @param fromContext            : this predicates context
     * @param to                     : the other term we are unifying it with, supposedly another predicate
     * @param toContext              : the context of the other term (predicate)
     * @param cc : the set of bindings between variables
     * @return true if the unification succeeds, false otherwise.
     */
    @Override
    public boolean unify(
            Context fromContext,
            Unifiable to,
            Context toContext,
            CodenotationConstraints cc
    ) {
        List<ContextualTerm> changes = new ArrayList<>();

        boolean res = attemptUnification(fromContext, to, toContext, changes, cc);
        if (!res) {
            for (ContextualTerm linkedVariable : changes) {
                cc.unlink(linkedVariable);
            }
        }

        return res;
    }

    private boolean attemptUnification(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> changes,
            CodenotationConstraints codenotationConstraint
    ){
        if (!(to instanceof Predicate)) {
            return false;
        }

        Predicate toPredicate = (Predicate) to;

        if (!this.sameName(toPredicate)) {
            return false;
        }

        int index = 0;
        for (Term term : this.getTerms()) {
            if (!term.attemptUnification(
                    fromContext,
                    toPredicate.getTerms().get(index++),
                    toContext,
                    changes,
                    codenotationConstraint
            )){
                return false;
            }
        }

        return true;
    }

    private Boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                       List<ContextualTerm> changes) {
        if (!(to instanceof Predicate)) {
            return false;
        }

        Predicate toPredicate = (Predicate) to;

        if (!this.sameName(toPredicate)) {
            return false;
        }

        int index = 0;
        for (Term term : this.getTerms()) {
            if (!term.attemptUnification(fromContext, toPredicate.getTerms().get(index++), toContext,
                    changes)
            ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Unifiable build(Context context) {
        return new Predicate(
                this.name,
                this.terms
                        .stream()
                        .map(term -> (Term) term.build(context))
                        .toList()
        );
    }

    public boolean sameName(Predicate predicate) {
        return this.getName().equals(predicate.getName());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Term> distinctTerms = terms.stream().distinct().toList();

        stringBuilder
                .append(this.name)
                .append("(");

        Integer termsCount = 0;
        for (Term term : distinctTerms) {
            stringBuilder.append(term);
            if (termsCount++ < distinctTerms.size() - 1) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
