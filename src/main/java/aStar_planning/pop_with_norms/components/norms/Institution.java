package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import logic.Action;
import logic.Constant;
import logic.Predicate;
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
    private List<Constant> concepts;
    private List<Predicate> assertions;
    private List<Action> possibleActions;
    private List<Norm> norms;
}