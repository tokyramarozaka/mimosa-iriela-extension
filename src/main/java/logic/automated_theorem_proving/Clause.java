package logic.automated_theorem_proving;

import logic.Atom;
import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Clause extends LogicalEntity {
    private List<Atom> atoms;

    public boolean hasSingleAtom(){
        return this.atoms.size() == 1;
    }

    public boolean hasNoAtoms(){
        return this.atoms.isEmpty();
    }

    public boolean hasMultipleAtoms(){
        return this.atoms.size() > 1;
    }

    @Override
    public LogicalEntity build(Context context) {
        List<Atom> builtAtoms = new ArrayList<>();

        this.getAtoms().forEach(atom -> {
            builtAtoms.add(atom.build(context));
        });

        return new Clause(builtAtoms);
    }

    @Override
    public LogicalEntity copy() {
        return new Clause(this.atoms.stream().toList());
    }

    @Override
    public String getLabel() {
        return this.toString();
    }
}
