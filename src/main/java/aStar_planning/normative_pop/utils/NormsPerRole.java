package aStar_planning.normative_pop.utils;

import aStar_planning.normative_pop.components.norms.Norm;
import aStar_planning.normative_pop.components.norms.Role;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * A role and all of its related norms
 */
@AllArgsConstructor
@ToString
public class NormsPerRole {
    private Role role;
    private List<Norm> norms;
}
