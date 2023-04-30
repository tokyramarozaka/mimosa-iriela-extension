package aStar_planning.normative_pop.norms;

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
