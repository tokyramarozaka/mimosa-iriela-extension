package validation_model;

import aStar.AStarResolver;
import aStar_planning.pop_with_norms.NormativePopPlanningProblem;
import mock_logic.validation_model.PlanningProblemFactory;
import org.junit.jupiter.api.Test;

public class validation_model_IT {
    private AStarResolver resolver;
    private NormativePopPlanningProblem inSearchForWoodAndFood = PlanningProblemFactory.
                                                                    haveFoodAndWood_asProvider();

    @Test
    public void findFoodAndWood_ok(){
        resolver = new AStarResolver(inSearchForWoodAndFood);

        resolver.outputSolutionPlan();
    }
}