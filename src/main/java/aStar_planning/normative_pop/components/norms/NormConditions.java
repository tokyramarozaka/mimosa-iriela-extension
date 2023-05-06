package aStar_planning.normative_pop.components.norms;

import logic.Atom;
import logic.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NormConditions {
    private List<Atom> conditions;

    public NormConditions build(Context context){
        return new NormConditions(
                this.conditions
                        .stream()
                        .map(atom -> atom.build(context))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int conditionsCount = 0;
        for(Atom condition : conditions){
            stringBuilder.append(condition);
            if (conditionsCount++ < this.conditions.size() - 1){
                stringBuilder.append(" , ");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
