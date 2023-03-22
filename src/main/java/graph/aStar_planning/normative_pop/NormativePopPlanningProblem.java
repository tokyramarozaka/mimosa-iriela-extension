package graph.aStar_planning.normative_pop;

import graph.aStar_planning.pop.PopPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.ToString;
import graph.aStar_planning.normative_pop.norms.Organization;

import java.util.List;

@ToString
public class NormativePopPlanningProblem extends PopPlanningProblem {
    private List<Organization> organizations;

    public NormativePopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                                       Goal goal, List<Organization> organizations) {
        super(initialSituation, possibleActions, goal);
        this.organizations = organizations;
    }
}
