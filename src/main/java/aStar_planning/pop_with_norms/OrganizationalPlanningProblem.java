package aStar_planning.pop_with_norms;

import aStar.State;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.utils.PlanInitializer;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.OrganizationalPlan;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class OrganizationalPlanningProblem extends PopPlanningProblem {
    private final OrganizationalPlan initialOrganizationalPlan;
    private final List<Organization> organizations;

    public OrganizationalPlanningProblem(
            Situation initialSituation,
            List<Action> possibleActions,
            Goal goal,
            List<Organization> organizations
    ){
        super(initialSituation, possibleActions, goal, true);
        this.organizations = organizations;
        this.initialOrganizationalPlan = PlanInitializer.constructInitialPlan(
                initialSituation,
                goal,
                organizations
        );
        this.getPossibleActions().addAll(getPossibleInstitutionalActions());
    }

    @Override
    public State getInitialState() {
        return this.initialOrganizationalPlan;
    }

    /**
     * Adds all the actions from the set of institutions organized in this planning problem.
     * This searches through all active organizations and fetches all their actions.
     * @return a list of all possible actions based upon all active organizations.
     */
    public List<Action> getPossibleInstitutionalActions(){
        List<Action> possibleActions = new ArrayList<>();

        for (Organization organization : this.organizations) {
            possibleActions.addAll(organization.getInstitution().getPossibleActions());
        }

        return possibleActions;
    }
}
