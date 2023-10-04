package iriela.description.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import iriela.description.constants.Agent;
import iriela.description.constants.Zones;
import iriela.description.institutions.Household;
import logic.Predicate;

import java.util.List;

public class HouseholdOrg {
    public static Organization get(){
        return new Organization(
                Household.get(),
                HouseholdOrg.norms(),
                HouseholdOrg.assertions()
        );
    }

    public static Organization get_haveFoodOnly(){
        return new Organization(
                Household.get_haveFoodOnly(),
                HouseholdOrg.norms(),
                HouseholdOrg.assertions()
        );
    }
    private static List<Norm> norms() {
        return List.of(
                new ConstitutiveNorm(Agent.SELF, Household.provider),
                new ConstitutiveNorm(Zones.A1, Household.land),
                new ConstitutiveNorm(Zones.A2, Household.land),
                new ConstitutiveNorm(Zones.A3, Household.forest),
                new ConstitutiveNorm(Zones.A4, Household.river),
                new ConstitutiveNorm(Zones.B1, Household.land),
                new ConstitutiveNorm(Zones.B2, Household.land),
                new ConstitutiveNorm(Zones.B3, Household.land),
                new ConstitutiveNorm(Zones.B4, Household.land),
                new ConstitutiveNorm(Zones.C1, Household.land),
                new ConstitutiveNorm(Zones.C2, Household.land),
                new ConstitutiveNorm(Zones.C3, Household.land),
                new ConstitutiveNorm(Zones.C4, Household.land),
                new ConstitutiveNorm(Zones.D1, Household.forest),
                new ConstitutiveNorm(Zones.D2, Household.river),
                new ConstitutiveNorm(Zones.D3, Household.river),
                new ConstitutiveNorm(Zones.D4, Household.forest)
        );
    }

    private static List<Predicate> assertions() {
        return List.of(
                Household.containsTrees(Zones.A3),
                Household.containsTrees(Zones.D1),
                Household.containsTrees(Zones.D4),
                Household.containsFishes(Zones.A4),
                Household.containsFishes(Zones.D2)
        );
    }
}
