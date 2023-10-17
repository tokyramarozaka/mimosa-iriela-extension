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
                Household.get_haveFishOnly(),
                HouseholdOrg.norms(),
                HouseholdOrg.assertions()
        );
    }

    public static Organization get_haveWoodOnly() {
        return new Organization(
                Household.get_haveWoodOnly(),
                HouseholdOrg.norms(),
                HouseholdOrg.assertions()
        );
    }

    private static List<Norm> norms() {
        return List.of(
                new ConstitutiveNorm(Agent.SELF, Household.provider)
        );
    }

    private static List<Predicate> assertions() {
        return List.of(
                Household.containsTrees(Zones.B1),
                Household.containsTrees(Zones.C1),
                Household.containsTrees(Zones.C2),

                Household.containsFishes(Zones.A3),
                Household.containsFishes(Zones.B3),
                Household.containsFishes(Zones.C3)
        );
    }

}
