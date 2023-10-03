package iriela.description.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import iriela.description.constants.Agent;
import iriela.description.constants.Zones;
import logic.Predicate;
import iriela.description.institutions.Village;

import java.util.ArrayList;
import java.util.List;

public class VillageOrg {
    public static Organization get(){
        return new Organization(
                Village.get(),
                VillageOrg.norms(),
                VillageOrg.assertions()
        );
    }

    private static List<Norm> norms() {
        return List.of(
                new ConstitutiveNorm(Agent.SELF, Village.member),
                new ConstitutiveNorm(Zones.C1, Village.sacred),
                new ConstitutiveNorm(Zones.D1, Village.sacred),
                new ConstitutiveNorm(Zones.D2, Village.sacred),
                new ConstitutiveNorm(Zones.C4, Village.sacred),
                new ConstitutiveNorm(Zones.A3, Village._protected),
                new ConstitutiveNorm(Zones.A4, Village._protected),
                new ConstitutiveNorm(Zones.C4, Village.office)
        );
    }

    private static List<Predicate> assertions() {
        return new ArrayList<>();
    }
}
