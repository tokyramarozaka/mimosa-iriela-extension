package mock_logic.validation_model.organizations;

import aStar_planning.pop_with_norms.components.Organization;
import mock_logic.validation_model.institutions.InstitutionFactory;
import mock_logic.validation_model.norms.ConstitutiveNormFactory;

import java.util.ArrayList;
import java.util.List;

public class OrganizationFactory {
    // TODO : build all organizations into a single list including household and exploiters
    public static List<Organization> getAllOrganizations() {
        return new ArrayList<>(List.of(

        ));
    }

    public static List<Organization> agentCountAsProvider_inHousehold() {
        return new ArrayList<>(List.of(
                new Organization(
                        InstitutionFactory.householdInstitution(),
                        ConstitutiveNormFactory.agentCountAsProvider()
                )
        ));
    }

    // TODO : build organizations for exploiters only.
    public static List<Organization> getExploitersOnly() {
        return new ArrayList<>(List.of(

        ));
    }
}
