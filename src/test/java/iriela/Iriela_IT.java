package iriela;

import aStar.AStarPlanner;
import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.Organization;
import graph.GraphvizGenerator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import iriela.description.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Iriela_IT {
    private static final Logger logger = LogManager.getLogger(Iriela_IT.class);
    @Test
    public void findsSolution_ok() throws IOException {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        AStarPlanner planner = new AStarPlanner(problem);

        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

//        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution_forFood_ok() throws IOException {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem_haveFoodOnly();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution_forWood_ok() throws IOException {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem_haveWoodOnly();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

}
