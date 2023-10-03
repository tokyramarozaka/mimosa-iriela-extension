package iriela;

import aStar.AStarPlanner;
import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativePlan;
import iriela.description.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class Iriela_IT {
    private static final Logger logger = LogManager.getLogger(Iriela_IT.class);
    @Test
    public void findsSolution_ok(){
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        AStarPlanner planner = new AStarPlanner(problem);

        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        planner.outputSolutionPlan();
    }
}
