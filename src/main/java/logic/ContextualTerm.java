package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ContextualTerm {
    private Context context;
    private Term term;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Context_");
        stringBuilder.append(this.getContext().getId());
        stringBuilder.append(":");
        stringBuilder.append(this.getTerm());

        return stringBuilder.toString();
    }
}