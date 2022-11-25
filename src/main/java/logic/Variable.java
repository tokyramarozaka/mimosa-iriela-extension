package logic;

import java.util.List;

public class Variable extends Term{
    public Variable(String name) {
        super(name);
    }

    @Override
    public boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                      List<ContextualTerm> currentChanges) {
        if(fromContext.equals(toContext)){
            return this.equals(to);
        }

        if(fromContext.isLinked(this)){
            ContextualTerm variableLink = fromContext.getLink(this);

            return variableLink
                    .getTerm()
                    .attemptUnification(variableLink.getContext(), to, toContext,currentChanges);
        }else{
            fromContext.link(this, (Term)to,toContext);
            currentChanges.add(new ContextualTerm(fromContext, this));
            return true;
        }
    }

    @Override
    public boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                      List<ContextualTerm> currentChanges,
                                      CodenotationConstraints codenotationConstraints
    ) {
        if (!codenotationConstraints.isCoherent()){
            return false;
        }

        if(fromContext.equals(toContext) && this.equals(to)){
            return true;
        }

        if (codenotationConstraints.isLinked(this, fromContext)){
            ContextualTerm variableLink = codenotationConstraints.getLink(this, fromContext);

            return variableLink.getTerm().unify(variableLink.getContext(),to,toContext);
        }

        Codenotation codenotationToAdd = new Codenotation(
                true,
                new ContextualTerm(fromContext,this),
                new ContextualTerm(toContext,(Term) to)
        );

        return codenotationConstraints.isAllowed(codenotationToAdd);
    }

    @Override
    public Unifiable build(Context context) {
        return context.build(this);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}