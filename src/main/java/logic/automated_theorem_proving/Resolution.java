package logic.automated_theorem_proving;

import exception.EmptyClauseException;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.Context;
import logic.LogicalInstance;
import logic.utils.ContextChange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class Resolution {
    private LogicalInstance clauseA;
    private Atom atomA;
    private LogicalInstance clauseB;
    private Atom atomB;
    private CodenotationConstraints codenotations;

    public LogicalInstance resolve(){
        // Copies the contexts to avoid side effects of the verification
        Context contextOneCopy = this.clauseA.getContext().copy();
        Context contextTwoCopy = this.clauseB.getContext().copy();

        // Update the codenotations for those copies accordingly
        Map<Context, Context> changes = new HashMap<>();
        changes.put(this.getClauseA().getContext(), contextOneCopy);
        changes.put(this.getClauseB().getContext(), contextTwoCopy);

        ContextChange contextChange = new ContextChange(changes);
        CodenotationConstraints codenotationsCopy = contextChange.apply(this.codenotations);

        if (!this.atomA.getPredicate().unify(
                contextOneCopy, this.atomB.getPredicate(), contextTwoCopy, codenotationsCopy)
        ){
            return null;
        }

        // Remove the atoms for the new clause
        Clause copyTwo = (Clause) clauseB.getLogicalEntity().copy();
        copyTwo.getAtoms().removeIf(other -> other.equals(this.atomB));

        // Check if the new clause is empty
        if (copyTwo.hasNoAtoms()){
            throw new EmptyClauseException();
        }

        // Return the new produced clause
        return new LogicalInstance(contextTwoCopy, new Clause(copyTwo.getAtoms()), codenotationsCopy);
    }
}
