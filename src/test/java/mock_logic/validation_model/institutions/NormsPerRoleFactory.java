package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import mock_logic.validation_model.institutions.village.RoleFactory;
import mock_logic.validation_model.norms.NormsForExploitationFarmer;
import mock_logic.validation_model.norms.NormsForHouseholdProvider;

import java.util.ArrayList;
import java.util.List;

public class NormsPerRoleFactory {
    public static List<NormsPerRole> forHousehold_Provider(){
        return new ArrayList<>(List.of(
            new NormsPerRole(RoleFactory.PROVIDER,NormsForHouseholdProvider.getAll())
        ));
    }

    public static List<NormsPerRole> forExploitation_Farmer(){
        return new ArrayList<>(List.of(
                new NormsPerRole(RoleFactory.FARMER,NormsForExploitationFarmer.getAll())
        ));
    }
}
