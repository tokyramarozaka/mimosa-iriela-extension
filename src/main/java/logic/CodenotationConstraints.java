package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of codenotations
 *
 * @see logic.Codenotation
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class CodenotationConstraints {
    private List<Codenotation> codenotations;

    public boolean isCoherent() {
        return true;
    }

    public ContextualTerm getLink(Variable variable, Context context) {
        ContextualTerm source = new ContextualTerm(context,variable);

        if(!isLinked(variable, context)){
            return source;
        }

        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.getLeftContextualTerm().equals(source))
                .findFirst()
                .get()
                .getRightContextualTerm();

    }

    public boolean isLinked(Variable variable, Context context) {
        ContextualTerm toTest = new ContextualTerm(context, variable);

        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.getLeftContextualTerm().equals(toTest))
                .count() > 0;
    }

    public boolean isAllowed(Codenotation codenotationToAdd) {
        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.matches(codenotationToAdd)
                        && codenotationToAdd.isCodenotation() != codenotationToAdd.isCodenotation())
                .count() == 0;
    }

    public CodenotationConstraints copy(){
        return new CodenotationConstraints(
                new ArrayList<>(this.getCodenotations())
        );
    }
}
