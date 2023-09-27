package iriela;

import aStar.Operator;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.NormativePlan;
import iriela.description.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Iriela_UT {
    private static final Logger logger = LogManager.getLogger(Iriela_UT.class);

    @Test
    public void getActiveOrganizations_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        NormativePlan initialState = (NormativePlan) problem.getInitialState();

        List<Organization> organizations = initialState.getOrganizations();
        logger.info(initialState);
        assertEquals(organizations.size(), 4);
    }

    @Test
    public void evaluateNormativeFlaws_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        Set<Flaw> initialFlaws = initialPlan.getFlaws();

        assertTrue(initialFlaws.size() > 0);
    }

    @Test
    public void getNormativeOptions_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();
        List<Operator> options = problem.getOptions(initialPlan);

        logger.info(options);
        logger.debug("=".repeat(10) + ". Now showing the initial plan.");
        logger.info(initialPlan);
    }

    @Test
    public void applyOperatorSolvesFlaw_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();
        Set<Flaw> initialFlaws = new HashSet<>(initialPlan.getFlaws());

        List<Operator> options = problem.getOptions(initialPlan);
        Operator toApply = options.get(0);
        logger.info("applying : " + toApply);
        initialPlan = (NormativePlan) initialPlan.applyPlanModification(toApply);
        logger.debug("got : " + initialPlan);
        Set<Flaw> nextFlaws = new HashSet<>(initialPlan.getFlaws());

        assertFalse(nextFlaws.contains(((PlanModification)toApply).getTargetFlaw()));
    }
}
