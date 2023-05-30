package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import mock_logic.validation_model.norms.RegulativeNorms_ForExploitation_Hunters;
import mock_logic.validation_model.norms.RegulativeNorms_ForHousehold_Providers;

import java.util.ArrayList;
import java.util.List;

public class NormsPerRoleFactory {
    public static List<NormsPerRole> forHousehold_Provider(){
        return new ArrayList<>(List.of(
            new NormsPerRole(
                    RoleFactory.providerRole(),
                    RegulativeNorms_ForHousehold_Providers.allRegulativeNorms()
            )
        ));
    }

    public static List<NormsPerRole> forExploitation_Hunter(){
        return new ArrayList<>(List.of(
                new NormsPerRole(
                        RoleFactory.hunterRole(),
                        RegulativeNorms_ForExploitation_Hunters.allRegulativeNorms()
                )
        ));
    }
}
