package aStar_planning.pop_with_norms.components;

import logic.Action;
import logic.Constant;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Institution {
    private String name;
    private float priority;
    private List<Constant> concepts;
    private List<Predicate> assertions;
    private List<Action> possibleActions;
    private List<Norm> norms;
}