package iriela;

import aStar.AStarPlanner;
import aStar_planning.pop_with_norms.NormativePlanningProblem;
import aStar_planning.pop_with_norms.components.NormativePlan;
import iriela.description.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Iriela_IT {
    private static final Logger logger = LogManager.getLogger(Iriela_IT.class);
    @Test
    public void findsSolution_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory.irielaProblem();
        AStarPlanner planner = new AStarPlanner(problem);

        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

//        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution_forWood__noNorms_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory
                .irielaProblem_haveWoodOnly_noVillageNorms();

        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    /* === OBLIGATIONS === */
    @Test
    public void findSolution_forFish_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory.irielaProblem_haveFishOnly();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution_forFish_withMandatoryFishReport_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory
                .irielaProblem_haveFish_withMandatoryFishReport();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    /* === PROHIBITIONS === */
    @Test
    public void findSolution_forFish__prohibitedTrespassing_inSacredZone_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory
                    .irielaProblem_noTrespassing();

        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution_forFish__prohibitedFishingWithoutLicense_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory
                .irielaProblem_noFishingWithoutLicense();

        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    /* === PERMISSIONS === */
    @Test
    public void findSolution_forWood__permittedCut_withLicense_ok() throws IOException {
        NormativePlanningProblem problem = PlanningProblemFactory.irielaProblem_haveWoodOnly();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }

    @Test
    public void findSolution__forFishingNet_permitted_withFishingLicense_ok() throws IOException{
        NormativePlanningProblem problem = PlanningProblemFactory
                .irielaProblem_haveFish_withRequiredLicenseForFishingNet();
        AStarPlanner planner = new AStarPlanner(problem);

        planner.outputSolutionPlan();
    }
}

