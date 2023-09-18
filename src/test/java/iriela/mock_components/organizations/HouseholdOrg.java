package iriela.mock_components.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import iriela.mock_components.constants.Agent;
import iriela.mock_components.institutions.Household;
import logic.Predicate;

import java.util.ArrayList;
import java.util.List;

public class HouseholdOrg {
    public static Organization get(){
        return new Organization(
                Household.get(),
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
        return new ArrayList<>();
    }
}
