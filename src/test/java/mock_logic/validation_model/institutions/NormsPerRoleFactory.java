package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import mock_logic.validation_model.norms.RegulativeNormFactory;

public class NormsPerRoleFactory {
    public static NormsPerRole forHouseholdMembers() {
        return new NormsPerRole(
                RoleFactory.providerRole(),
                RegulativeNormFactory.providerNorms()
        );
    }

    public static NormsPerRole forExploitationHunters() {
        return new NormsPerRole(
                RoleFactory.hunterRole(),
                RegulativeNormFactory.hunterNorms()
        );
    }
}
