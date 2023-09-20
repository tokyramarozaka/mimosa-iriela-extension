package iriela.description.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import iriela.description.constants.Agent;
import iriela.description.constants.Zones;
import logic.Predicate;
import iriela.description.institutions.Village;

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
                new ConstitutiveNorm(Zones.A1, Village.land),
                new ConstitutiveNorm(Zones.A2, Village.land),
                new ConstitutiveNorm(Zones.A3, Village.forest),
                new ConstitutiveNorm(Zones.A4, Village.river),
                new ConstitutiveNorm(Zones.B1, Village.land),
                new ConstitutiveNorm(Zones.B2, Village.land),
                new ConstitutiveNorm(Zones.B3, Village.land),
                new ConstitutiveNorm(Zones.B4, Village.land),
                new ConstitutiveNorm(Zones.C1, Village.land),
                new ConstitutiveNorm(Zones.C2, Village.land),
                new ConstitutiveNorm(Zones.C3, Village.land),
                new ConstitutiveNorm(Zones.C4, Village.office),
                new ConstitutiveNorm(Zones.D1, Village.forest),
                new ConstitutiveNorm(Zones.D2, Village.river),
                new ConstitutiveNorm(Zones.D3, Village.river),
                new ConstitutiveNorm(Zones.D4, Village.forest),
                new ConstitutiveNorm(Zones.C1, Village.sacred),
                new ConstitutiveNorm(Zones.D1, Village.sacred),
                new ConstitutiveNorm(Zones.D2, Village.sacred),
                new ConstitutiveNorm(Zones.A3, Village._protected),
                new ConstitutiveNorm(Zones.A4, Village._protected)
        );
    }

    private static List<Predicate> assertions() {
        return List.of(
                Village.hasTrees(Zones.A3),
                Village.hasTrees(Zones.D1),
                Village.hasTrees(Zones.D4),
                Village.hasFish(Zones.A4),
                Village.hasFish(Zones.D2)
        );
    }
}
