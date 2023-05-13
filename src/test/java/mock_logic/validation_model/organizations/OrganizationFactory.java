package mock_logic.validation_model.organizations;

import aStar_planning.pop_with_norms.components.Organization;
import mock_logic.validation_model.institutions.InstitutionFactory;
import mock_logic.validation_model.norms.ConstitutiveNormFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrganizationFactory {
    public static List<Organization> agentCountAsProvider_inHousehold() {
        return new ArrayList<>(List.of(
                new Organization(
                        InstitutionFactory.householdInstitution(),
                        ConstitutiveNormFactory.agentCountAsProvider()
                )
        ));
    }

    public static List<Organization> agentCountAsExploiter_inExploitation() {
        return new ArrayList<>(List.of(
                new Organization(
                        InstitutionFactory.exploitationInstitution(),
                        ConstitutiveNormFactory.agentCountAsExploiter()
                )
        ));
    }

    public static List<Organization> agentCountAs_providerAndExploiter() {
        return Stream.concat(
                agentCountAsProvider_inHousehold().stream(),
                agentCountAsExploiter_inExploitation().stream()
        ).collect(Collectors.toList());
    }
}
