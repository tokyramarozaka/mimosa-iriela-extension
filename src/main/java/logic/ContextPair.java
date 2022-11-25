package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ContextPair {
    private Variable variable;
    private ContextualTerm contextualTerm;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.variable);
        stringBuilder.append(" ==> ");
        stringBuilder.append(this.contextualTerm);

        return stringBuilder.toString();
    }
}
