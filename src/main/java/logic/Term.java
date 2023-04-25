package logic;

import constraints.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
public abstract class Term implements Unifiable {
    private String name;

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext) {
        List<ContextualTerm> lst = new ArrayList<>();

        boolean res = attemptUnification(fromContext, to, toContext, lst);

        if (!res) {
            for (ContextualTerm var : lst) {
                var.getContext().unlink((Variable) var.getTerm());
            }
        }

        return res;
    }

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext,
                         CodenotationConstraints cc) {
        List<ContextualTerm> lst = new ArrayList<>();

        boolean res = attemptUnification(fromContext, to, toContext, lst, cc);

        if (!res) {
            for (ContextualTerm linkedVariable : lst) {
                cc.unlink(linkedVariable);
            }
        }

        return res;
    }

    public abstract boolean attemptUnification(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> currentChanges
    );

    /**
     * Attempts a unification between the current term and another one using the codenotation
     * constraints provided.
     * @param fromContext : this term's context
     * @param to : the other unifiable we try to unify it with
     * @param toContext : the context of the other term
     * @param currentChanges :
     * @param codenotationConstraints : the current set of codenotation constraints which binds
     *                                variables to values
     * @return true if the unification succeeds, and false otherwise.
     */
    public abstract boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                               List<ContextualTerm> currentChanges,
                                               CodenotationConstraints codenotationConstraints);

    public boolean sameName(Term term) {
        return this.name.equals(term.getName());
    }

    public abstract boolean testEqual(Context fromContext, Term other, Context otherContext);

    public abstract boolean testEqual(Context fromContext, Term other, Context otherContext,
                                      CodenotationConstraints cc);

    @Override
    public String toString() {
        return this.name;
    }
}
