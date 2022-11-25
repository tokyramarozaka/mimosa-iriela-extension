package planning;

import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Problem {
    private Situation initialSituation;
    private List<Action> possibleActions;
    private Goal goal;
}
