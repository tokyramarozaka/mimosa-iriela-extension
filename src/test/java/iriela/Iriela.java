package iriela;

import aStar.AStarResolver;
import aStar_planning.pop_with_norms.OrganizationalPlanningProblem;
import mock_logic.validation_model.PlanningProblemFactory;

public class Iriela {
    private AStarResolver resolver;
    private OrganizationalPlanningProblem iriela = PlanningProblemFactory.
            irielaProblem();

}
