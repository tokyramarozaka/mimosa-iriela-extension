package aStar_planning.pop_with_norms.components;

import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class RoleActions {
    private Role role;
    private List<Action> actions;
}
