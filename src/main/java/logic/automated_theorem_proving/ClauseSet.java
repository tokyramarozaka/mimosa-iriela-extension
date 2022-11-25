package logic.automated_theorem_proving;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClauseSet {
    private List<Clause> clauses;
    public void add(Clause toAdd) {
        this.clauses.add(toAdd);
    }
}
