package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of atoms describing the consequenceS of an action. The set of atoms describe :
 * <ul>
 *     <li> an add-list : the facts which will come true after the action</li>
 *     <li> a delete-list : the facts which are no longer true after the action.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ActionConsequence {
    private List<Atom> atoms;

    public ActionConsequence(){
        this.atoms = new ArrayList<>();
    }

    public ActionConsequence build(Context context){
        return new ActionConsequence(
                this.atoms.stream()
                        .map(atom -> atom.build(context))
                        .toList()
        );
    }

    public ActionConsequence copy() {
        return new ActionConsequence(this.atoms.stream().toList());
    }
}
