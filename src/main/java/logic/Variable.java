package logic;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Role;
import constraints.Codenotation;
import constraints.CodenotationConstraints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Variable extends Term {
    private final static Logger logger = LogManager.getLogger(Variable.class);

    public Variable(String name) {
        super(name);
    }

    @Override
    public boolean attemptUnification(Context fromContext, Unifiable to, Context toContext,
                                      List<ContextualTerm> currentChanges) {
        if (fromContext.equals(toContext)) {
            return this.equals(to);
        }

        if (fromContext.isLinked(this)) {
            ContextualTerm variableLink = fromContext.getLink(this);

            return variableLink
                    .getTerm()
                    .attemptUnification(variableLink.getContext(), to, toContext, currentChanges);
        } else {
            fromContext.link(this, (Term) to, toContext);
            currentChanges.add(new ContextualTerm(fromContext, this));
            return true;
        }
    }

    @Override
    public boolean attemptUnification(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> currentChanges,
            CodenotationConstraints cc
    ) {
        if (!cc.isCoherent()) {
            return false;
        }

        if (fromContext.equals(toContext) && this.equals(to)) {
            return true;
        }

        if (cc.isLinked(this, fromContext)) {
            ContextualTerm variableLink = cc.getLink(this, fromContext);

            return variableLink
                    .getTerm()
                    .unify(variableLink.getContext(), to, toContext, cc);
        }

        if (!cc.isAllowed(new Codenotation(
                true,
                new ContextualTerm(fromContext, this),
                new ContextualTerm(toContext, (Term) to))
        )) {
            return false;
        }

        cc.link(this, fromContext, (Term) to, toContext);
        currentChanges.add(new ContextualTerm(fromContext, this));
        return cc.isCoherent();
    }

    public boolean attemptUnificationWithConstitutiveNorms(
            Context fromContext,
            Unifiable to,
            Context toContext,
            List<ContextualTerm> currentChanges,
            CodenotationConstraints cc,
            List<Role> variableRoles,
            List<ConstitutiveNorm> constitutiveNorms
    ) {
        if (!cc.isCoherent()) {
            return false;
        }

        if (fromContext.equals(toContext) && this.equals(to)) {
            return true;
        }

        if (cc.isLinked(this, fromContext)) {
            ContextualTerm variableLink = cc.getLink(this, fromContext);

            return variableLink
                    .getTerm()
                    .unify(variableLink.getContext(), to, toContext, cc);
        }

        Codenotation toAdd = new Codenotation(
                true,
                new ContextualTerm(fromContext, this),
                new ContextualTerm(toContext, (Term) to));

        if (!cc.isAllowed(toAdd)) {
            return false;
        }

        /**
         * Codenotation : L and R terms.
         * check which roles L must play inside the step's role
         * and then see if R actually plays that role
         * (BASIC TYPE checking)
         * Given a list of roles that this variable must have, we can go like ...
         * for each role that the variable must play,
         * does the constitutive norms says that R is playing that role ?
         * return constitutiveNorms
         *      .stream()
         *      .anyMatch(constitutiveNorm -> constitutiveNorm.getSource().sameName(R.getTerm())
         *              && constitutiveNorm.getTarget().equals(role))
         */
        cc.link(this, fromContext, (Term) to, toContext);
        currentChanges.add(new ContextualTerm(fromContext, this));
        return cc.isCoherent();
    }

    @Override
    public boolean testEqual(Context fromContext, Term other, Context otherContext) {
        if (this.equals(other) && fromContext.equals(otherContext))
            return true;
        if (other instanceof Constant)
            return other.testEqual(otherContext, this, fromContext);
        else if (other instanceof Variable) {
            if (!fromContext.isLinked(this) && !otherContext.isLinked((Variable) other))
                return (this.sameName(other) && fromContext.getId().equals(otherContext.getId()));
            else if (fromContext.isLinked(this))
                return fromContext.getLink(this).getTerm().testEqual(
                        fromContext.getLink(this).getContext(),
                        other,
                        otherContext
                );
            return otherContext.getLink((Variable) other).getTerm().testEqual(
                    otherContext.getLink((Variable) other).getContext(), this, fromContext
            );
        } else if (fromContext.isLinked(this))
            return fromContext.getLink(this).getTerm().testEqual(fromContext, other, otherContext);
        return false;
    }

    @Override
    public boolean testEqual(Context fromContext, Term other, Context otherContext,
                             CodenotationConstraints cdn
    ) {
        if (this.sameName(other) && fromContext.getId() == (otherContext.getId())) {
            return true;
        }

        if (other instanceof Constant)
            return other.testEqual(otherContext, this, fromContext, cdn);

        if (other instanceof Variable) {
            if (!cdn.isLinked(this, fromContext) && !cdn.isLinked(other, otherContext)) {
                return (this.sameName(other) && fromContext.getId() == (otherContext.getId()));
            } else if (cdn.isLinked(this, fromContext))
                if (cdn.getLink(this, fromContext).getTerm().testEqual(
                        cdn.getLink(this, fromContext).getContext(),
                        other,
                        otherContext, cdn
                )) {
                    return true;
                } else if (cdn.isLinked(other, otherContext)) {
                    if (this.testEqual(fromContext, cdn.getLink(other, otherContext).getTerm(), cdn.getLink(other, otherContext).getContext(), cdn))
                        return true;
                }
        }

        return false;
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