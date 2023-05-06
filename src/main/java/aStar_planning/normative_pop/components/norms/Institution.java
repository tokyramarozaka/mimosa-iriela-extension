package aStar_planning.normative_pop.components.norms;

import aStar_planning.normative_pop.utils.NormsPerRole;
import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Institution {
    private String name;
    private NormsPerRole normsPerRole;
    private List<Action> possibleActions;
}