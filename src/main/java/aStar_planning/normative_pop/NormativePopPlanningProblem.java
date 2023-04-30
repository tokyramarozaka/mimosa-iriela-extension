package aStar_planning.normative_pop;

import aStar_planning.pop.PopPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import aStar_planning.normative_pop.norms.Organization;

import java.util.List;

@Getter
@ToString
public class NormativePopPlanningProblem extends PopPlanningProblem {
    private final List<Organization> organizations;

    public NormativePopPlanningProblem(
            Situation initialSituation,
            List<Action> possibleActions,
            Goal goal,
            List<Organization> organizations
    ){
        super(initialSituation, possibleActions, goal);
        this.organizations = organizations;
    }
}
