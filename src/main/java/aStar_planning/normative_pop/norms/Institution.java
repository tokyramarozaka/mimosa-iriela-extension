package aStar_planning.normative_pop.norms;

import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Institution {
    private String name;
    private NormsPerRole normsPerRole;
    private List<Action> possibleActions;
}