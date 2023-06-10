package validation_model;

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

import java.util.List;

public class validation_model_UT {
    private static final Logger logger = LogManager.getLogger(validation_model_UT.class);

    @Test
    public void getActiveOrganizations_ok() {
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialState = problem.getInitialState();

        List<Organization> activeOrganizations = initialState.getActiveOrganizations();

        Assertions.assertEquals(activeOrganizations.size(), 1);
        Assertions.assertTrue(activeOrganizations.get(0).hasRole(Keywords.AGENT_CONCEPT));
    }

    @Test
    public void detectDummyObligations_ok() {
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialPlan = problem.getInitialState();

        List<NormativeFlaw> normativeFlaws = initialPlan.getFlaws().stream()
                .filter(flaw -> flaw instanceof NormativeFlaw)
                .map(flaw -> (NormativeFlaw) flaw)
                .toList();

        Assertions.assertEquals(2, normativeFlaws.size());
        Assertions.assertTrue(normativeFlaws.stream()
                .allMatch(flaw -> flaw.getSituation().equals(initialPlan.getInitialSituation())
                        || flaw.getSituation().equals(initialPlan.getFinalSituation())));
        for (NormativeFlaw flaw : normativeFlaws) {
            Assertions.assertEquals(
                    DeonticOperator.OBLIGATION,
                    flaw.getFlawedNorm().getDeonticOperator()
            );
        }
        logger.info(initialPlan);
    }
}
