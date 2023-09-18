package iriela;

import aStar.Operator;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop_with_norms.NormativePopPlanningProblem;
import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.OrganizationalPlan;
import mock_logic.validation_model.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import settings.Keywords;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class validation_model_UT {
    private static final Logger logger = LogManager.getLogger(validation_model_UT.class);

    @Test
    public void getActiveOrganizations_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        OrganizationalPlan initialState = (OrganizationalPlan) problem.getInitialState();

        List<Organization> organizations = initialState.getOrganizations();

        Assertions.assertEquals(organizations.size(), 4);
    }

    @Test
    public void getNormativeOptions_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        OrganizationalPlan initialPlan = (OrganizationalPlan) problem.getInitialState();

        logger.info(problem.getOptions(initialPlan));
    }

    @Test
    public void applyOperatorSolvesFlaw_ok() {
        OrganizationalPlanningProblem problem = PlanningProblemFactory.irielaProblem();
        OrganizationalPlan initialPlan = (OrganizationalPlan) problem.getInitialState();

        Set<Flaw> initialFlaws = new HashSet<>(initialPlan.getFlaws());
        List<Operator> options = problem.getOptions(initialPlan);

        for (Operator planModification : options) {
            initialPlan = (OrganizationalPlan) initialPlan.applyPlanModification(planModification);
        }
        Set<Flaw> nextFlaws = new HashSet<>(initialPlan.getFlaws());


        initialFlaws.stream()
                .filter(flaw -> flaw instanceof NormativeFlaw)
                .forEach(initialFlaw -> Assertions.assertFalse(nextFlaws.contains(initialFlaw)));
    }
}
