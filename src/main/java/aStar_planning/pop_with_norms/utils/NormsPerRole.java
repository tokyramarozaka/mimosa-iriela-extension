package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop_with_norms.components.norms.Norm;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.components.norms.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A role and all of its related norms
 */
@AllArgsConstructor
@Getter
@ToString
public class NormsPerRole {
    private Role role;
    private List<RegulativeNorm> regulativeNorms;
}
