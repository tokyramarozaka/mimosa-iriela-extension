package mock_logic.validation_model.organizations;

import aStar_planning.pop_with_norms.components.norms.Organization;
import mock_logic.validation_model.institutions.InstitutionFactory;
import mock_logic.validation_model.norms.ConstitutiveNormFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrganizationFactory {
    public static List<Organization> householdOrganizationDetails() {
        return new ArrayList<>(List.of(
                new Organization(
                        InstitutionFactory.householdInstitution(),
                        ConstitutiveNormFactory.householdOrganization()
                )
        ));
    }

    public static List<Organization> exploitationOrganizationDetails() {
        return new ArrayList<>(List.of(
                new Organization(
                        InstitutionFactory.exploitationInstitution(),
                        ConstitutiveNormFactory.exploitationOrganization()
                )
        ));
    }

    public static List<Organization> allOrganizations() {
        return Stream.concat(
                householdOrganizationDetails().stream(),
                exploitationOrganizationDetails().stream()
        ).collect(Collectors.toList());
    }
}
