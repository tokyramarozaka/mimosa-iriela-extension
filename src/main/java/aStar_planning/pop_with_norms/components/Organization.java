package aStar_planning.pop_with_norms.components;

import aStar_planning.pop_with_norms.Concept;
import aStar_planning.pop_with_norms.components.norms.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.norms.Institution;
import aStar_planning.pop_with_norms.components.norms.Norm;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.components.norms.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Organization {
    private List<ConstitutiveNorm> constitutiveNorms;

    /**
     * Checks if some concept plays a role in this current organization
     * @param someConcept : the concept which we want to check if it plays a role
     * @return true if the concept is plays a role in the current organization, and false otherwise
     */
    public boolean hasRole(Concept someConcept) {
        return this.constitutiveNorms
                .stream()
                .anyMatch(constitutiveNorm -> someConcept.equals(constitutiveNorm.getSource()));
    }

}