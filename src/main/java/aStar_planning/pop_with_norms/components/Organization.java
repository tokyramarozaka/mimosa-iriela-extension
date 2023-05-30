package aStar_planning.pop_with_norms.components;

import aStar_planning.pop_with_norms.Concept;
import aStar_planning.pop_with_norms.components.norms.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.norms.Institution;
import aStar_planning.pop_with_norms.components.norms.Norm;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.components.norms.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Organization {
    private Institution institution;
    private List<ConstitutiveNorm> constitutiveNorms;

    public List<RegulativeNorm> getRegulativeNormsByConcept(Concept concept) {
        List<RegulativeNorm> regulativeNorms = new ArrayList<>();

        this.constitutiveNorms.stream()
                .filter(constitutiveNorm -> constitutiveNorm.getSource().equals(concept))
                .forEach(constitutiveNorm -> {
                    Role playedRole = constitutiveNorm.getTarget();
                    regulativeNorms.addAll(this.getInstitution().getNormsForRole(playedRole));
                });

        return regulativeNorms;
    }

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

    @Override
    public String toString() {
        return this.institution+
                "\nORGANIZATION\n\t" +
                this.getConstitutiveNorms() +
                " IN " +
                this.getInstitution().getName();
    }
}