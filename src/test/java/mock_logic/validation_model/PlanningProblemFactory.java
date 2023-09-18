package mock_logic.validation_model;

import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.Organization;
import logic.Action;
import logic.Goal;
import logic.Situation;
import mock_logic.validation_model.organizations.ExploitationOrg;
import mock_logic.validation_model.organizations.GlobalOrg;
import mock_logic.validation_model.organizations.HouseholdOrg;
import mock_logic.validation_model.organizations.VillageOrg;

import java.util.ArrayList;
import java.util.List;

public class PlanningProblemFactory {
    public static OrganizationalPlanningProblem irielaProblem(){
        return new OrganizationalPlanningProblem(
                new Situation(),
                new ArrayList<Action>(),
                new Goal(),
                new ArrayList<Organization>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get(),
                        HouseholdOrg.get(),
                        ExploitationOrg.get()
                ))
        );
    }
}
