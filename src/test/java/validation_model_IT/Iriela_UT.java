package validation_model_IT;

import aStar.AStarResolver;
import aStar_planning.pop_with_norms.NormativePopPlanningProblem;
import aStar_planning.pop_with_norms.components.NormativePlan;
import aStar_planning.pop_with_norms.components.Organization;
import mock_logic.validation_model.PlanningProblemFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import settings.Keywords;

import java.util.List;

public class Iriela_UT {
    private static final Logger logger = LogManager.getLogger(Iriela_UT.class);
    @Test
    public void getActiveOrganizations_ok(){
        NormativePopPlanningProblem problem = PlanningProblemFactory.haveFoodAndWood_asProvider();
        NormativePlan initialState = (NormativePlan) problem.getInitialState();

        List<Organization> activeOrganizations = initialState.getActiveOrganizations();

        logger.info("active organizations are : " + initialState.getActiveOrganizations());
        Assertions.assertEquals(activeOrganizations.size(), 1);
        Assertions.assertTrue(activeOrganizations.get(0).hasRole(Keywords.AGENT_CONCEPT));
    }

    @Test
    public void evaluateNormativeFlaws_ok(){

    }
}
