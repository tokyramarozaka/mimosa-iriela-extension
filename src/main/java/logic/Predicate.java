package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Predicate implements Unifiable{
    private String name;
    private List<Term> terms;

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext) {
        List<ContextualTerm> changes = new ArrayList<>();
        Boolean unifyAttemptSuccess = attemptUnify(fromContext,to,toContext,changes);

        if(!unifyAttemptSuccess){
            for (ContextualTerm change : changes) {
                change.getContext().unlink((Variable) change.getTerm());
            }
        }

        return unifyAttemptSuccess;
    }

    private Boolean attemptUnify(Context fromContext, Unifiable to, Context toContext, List<ContextualTerm> changes) {
        if (!(to instanceof Predicate)){
            return false;
        }

        Predicate toPredicate = (Predicate) to;

        if (!this.sameName(toPredicate)){
            return false;
        }

        int index = 0;
        for(Term term:this.getTerms()){
            if (!term.attemptUnification(
                    fromContext,toPredicate.getTerms().get(index++),toContext,changes)) {
                        return false;
            }
        }

        return true;
    }

    /**
     * TODO : unification through codenotation constraints
     * @param fromContext
     * @param to
     * @param toContext
     * @param codenotationConstraint
     * @return
     */
    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext,
                         CodenotationConstraints codenotationConstraint) {
        return false;
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
        for(Term term : distinctTerms){
            stringBuilder.append(term);
            if (termsCount++ < distinctTerms.size() - 1){
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
