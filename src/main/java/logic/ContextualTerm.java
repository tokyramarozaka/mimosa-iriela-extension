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

        stringBuilder.append(this.getTerm());
        stringBuilder.append("::");
        stringBuilder.append(this.getContext().getId());

        return stringBuilder.toString();
    }
}