package logic;

import constraints.Codenotation;
import constraints.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of atoms which describes a set of preconditions for an action to be executed. Mainly used
 * to check if an action can be executed in a certain situation.
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ActionPrecondition {
    private List<Atom> atoms;

    public ActionPrecondition(){
        this.atoms = new ArrayList<>();
    }

    public ActionPrecondition build(Context context) {
        return new ActionPrecondition(
                this.atoms
                        .stream()
                        .map(atom -> atom.build(context))
                        .toList()
        );
    }

    public ActionPrecondition build(Context context, CodenotationConstraints cc) {
        return new ActionPrecondition(
                this.atoms
                        .stream()
                        .map(atom -> atom.build(context, cc))
                        .toList()
        );
    }

    public List<Term> getTerms(){
        List<Term> terms = new ArrayList<>();

        for (Atom atom : this.getAtoms()) {
            for (Term term : atom.getPredicate().getTerms()) {
                if(!terms.contains(term)){
                    terms.add(term);
                }
            }
        }

        return terms;
    }
    public ActionPrecondition copy() {
        return new ActionPrecondition(this.atoms.stream().toList());
    }
}
