package validation_model;

import aStar.Operator;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop_with_norms.NormativePopPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativeFlaw;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.norms.DeonticOperator;
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
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialState = (NormativePlan) problem.getInitialState();

        List<Organization> activeOrganizations = initialState.getActiveOrganizations();

        Assertions.assertEquals(activeOrganizations.size(), 1);
        Assertions.assertTrue(activeOrganizations.get(0).hasRole(Keywords.AGENT_CONCEPT));
    }

    @Test
    public void detectDummyObligations_ok() {
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        List<NormativeFlaw> normativeFlaws = initialPlan.getFlaws().stream()
                .filter(flaw -> flaw instanceof NormativeFlaw)
                .map(flaw -> (NormativeFlaw) flaw)
                .toList();

        Assertions.assertEquals(2, normativeFlaws.size());
        Assertions.assertTrue(normativeFlaws.stream()
                .allMatch(flaw -> flaw.getApplicableSituation().equals(initialPlan.getInitialSituation())
                        || flaw.getApplicableSituation().equals(initialPlan.getFinalSituation())));

        for (NormativeFlaw flaw : normativeFlaws) {
            Assertions.assertEquals(
                    DeonticOperator.OBLIGATION,
                    flaw.getFlawedNorm().getDeonticOperator()
            );
        }
        logger.info(initialPlan);
    }

    @Test
    public void getNormativeOptions_ok() {
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        logger.info(problem.getOptions(initialPlan));
    }

    @Test
    public void applyOperatorSolvesFlaw_ok() {
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();
        Set<Flaw> initialFlaws = new HashSet<>(initialPlan.getFlaws());
        List<Operator> options = problem.getOptions(initialPlan);

        for (Operator planModification : options) {
            initialPlan = (NormativePlan) initialPlan.applyPlanModification(planModification);
        }
        Set<Flaw> nextFlaws = new HashSet<>(initialPlan.getFlaws());


        initialFlaws.stream()
                .filter(flaw -> flaw instanceof NormativeFlaw)
                .forEach(initialFlaw -> Assertions.assertFalse(nextFlaws.contains(initialFlaw)));
    }
}
