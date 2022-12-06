package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
public abstract class Term implements Unifiable{
    private String name;

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext) {
        List<ContextualTerm> lst = new ArrayList<>();

        boolean res = attemptUnification(fromContext,to,toContext,lst);

        if (!res) {
            for (ContextualTerm var:lst) {
                var.getContext().unlink((Variable)var.getTerm());
            }
        }

        return res;
    }

    @Override
    public boolean unify(Context fromContext, Unifiable to, Context toContext,
                         CodenotationConstraints cc) {
        List<ContextualTerm> lst = new ArrayList<>();

        boolean res = attemptUnification(fromContext,to,toContext,lst,cc);

        if (!res) {
            for (ContextualTerm linkedVariable : lst) {
                cc.unlink(linkedVariable);
            }
        }

        return res;
    }

    public abstract boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                               List<ContextualTerm> currentChanges);

    public abstract boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                               List<ContextualTerm> currentChanges,
                                               CodenotationConstraints codenotationConstraints);

    public boolean sameName(Term term){
        return this.name.equals(term.getName());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
