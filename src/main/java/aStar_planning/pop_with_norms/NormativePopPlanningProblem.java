package aStar_planning.pop_with_norms;

import aStar.AStarProblem;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import aStar_planning.pop_with_norms.components.Organization;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class NormativePopPlanningProblem extends PopPlanningProblem {
    private final List<Organization> organizations;
    private NormativePlan initialNormativePlan;

    public NormativePopPlanningProblem(
            Situation initialSituation,
            List<Action> possibleActions,
            Goal goal,
            List<Organization> organizations
    ){
        super(initialSituation, possibleActions, goal);
        this.organizations = organizations;
        this.initialNormativePlan = constructInitialNormativePlan();
    }

    private NormativePlan constructInitialNormativePlan() {
        return new NormativePlan(this.getInitialPlan(), this.organizations, new ArrayList<>());
    }

    @Override
    public NormativePlan getInitialState() {
        return this.initialNormativePlan;
    }
}
