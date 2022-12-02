package aStar_planning.normative_pop;

import aStar_planning.pop.PopPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.ToString;
import norms.RegulativeNorm;

import java.util.List;

@ToString
public class NormativePopPlanningProblem extends PopPlanningProblem {
    private List<RegulativeNorm> regulativeNorms;

    public NormativePopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                                       Goal goal, List<RegulativeNorm> regulativeNorms) {
        super(initialSituation, possibleActions, goal);
        this.regulativeNorms = regulativeNorms;
    }
}
