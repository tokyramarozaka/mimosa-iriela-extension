package aStar_planning.normative_pop;

import aStar_planning.pop.PopPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.ToString;
import norms.Norm;

import java.util.List;

@ToString
public class NormativePopPlanningProblem extends PopPlanningProblem {
    private List<Norm> norms;

    public NormativePopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                                       Goal goal, List<Norm> norms) {
        super(initialSituation, possibleActions, goal);
        this.norms = norms;
    }
}
