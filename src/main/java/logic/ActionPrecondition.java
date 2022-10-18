package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * A set of atoms which describes a set of preconditions for an action to be executed. Mainly used
 * to check if an action can be executed in a certain situation.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ActionPrecondition {
    private List<Atom> atoms;

    public ActionPrecondition build(Context context) {
        return new ActionPrecondition(
                this.atoms
                        .stream()
                        .map(atom -> atom.build(context))
                        .toList()
        );
    }

    public ActionPrecondition copy() {
        return new ActionPrecondition(this.atoms.stream().toList());
    }
}
