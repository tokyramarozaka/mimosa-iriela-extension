package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * A role and all of its related norms
 */
@AllArgsConstructor
@Getter
public class NormsPerRole {
    private Role role;
    private List<Norm> norms;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("\n\t")
                .append(this.role)
                .append("NORMS :");

        this.norms.forEach(regulativeNorm -> stringBuilder
                .append("\n\t\t- ")
                .append(regulativeNorm));

        return stringBuilder.toString();
    }
}
