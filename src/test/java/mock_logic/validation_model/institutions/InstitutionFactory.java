package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.utils.NormsPerRole;
import logic.Action;
import mock_logic.validation_model.ActionFactory;

import java.util.List;

/**
 * A class to generate Institutions for unit testing. In this context, we have the following
 * institutions :
 * <ul>
 *     <li>Household : representing all the agent needs in terms of everyday life including food and
 *     wood</li>
 *     <li>Exploitation : representing some regulated exploitation of a resource</li>
 * </ul>
 */
public class InstitutionFactory {
    public static Institution householdInstitution(){
        List<NormsPerRole> normsPerRoles = NormsPerRoleFactory.forHousehold_Provider();
        List<Action> householdActions = ActionFactory.allActionsInHousehold();
        return new Institution("HOUSEHOLD", normsPerRoles, householdActions, 9);
    }

    public static Institution exploitationInstitution(){
        List<NormsPerRole> normsPerRoles = NormsPerRoleFactory.forExploitation_Farmer();
        List<Action> exploitationActions = ActionFactory.allActionsInExploitation();
        return new Institution("EXPLOITATION", normsPerRoles, exploitationActions, 7);
    }
}

