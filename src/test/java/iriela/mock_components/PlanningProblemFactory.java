package iriela.mock_components;

import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.Organization;
import logic.Action;
import logic.Goal;
import logic.Situation;
import iriela.mock_components.organizations.ExploitationOrg;
import iriela.mock_components.organizations.GlobalOrg;
import iriela.mock_components.organizations.HouseholdOrg;
import iriela.mock_components.organizations.VillageOrg;

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
