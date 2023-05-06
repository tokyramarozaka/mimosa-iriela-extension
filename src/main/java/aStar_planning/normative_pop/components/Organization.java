package aStar_planning.normative_pop.components;

import aStar_planning.normative_pop.components.norms.ConstitutiveNorm;
import aStar_planning.normative_pop.components.norms.Institution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Organization {
    private Institution institution;
    private List<ConstitutiveNorm> constitutiveNorms;
}