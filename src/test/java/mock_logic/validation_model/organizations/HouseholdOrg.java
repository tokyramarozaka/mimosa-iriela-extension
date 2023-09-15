package mock_logic.validation_model.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import logic.Predicate;
import mock_logic.validation_model.constants.Agent;
import mock_logic.validation_model.institutions.Household;

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
