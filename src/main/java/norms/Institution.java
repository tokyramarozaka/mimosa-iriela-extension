package norms;

import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Institution {
    private String name;
    private Map<Role, List<Norm>> norms;
    private List<Action> possibleActions;
}