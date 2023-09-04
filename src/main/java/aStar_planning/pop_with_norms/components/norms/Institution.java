package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Getter
@ToString
public class Institution {
    private String name;
    private float priority;
    private List<Role> roles;
    private List<RegulativeNorm> regulativeNorms;
    private List<Action> possibleActions;
}