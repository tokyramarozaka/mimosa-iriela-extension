package mock_logic.validation_model;

import aStar_planning.pop_with_norms.NormativePopPlanningProblem;

import java.util.ArrayList;

public class PlanningProblemFactory {
    public static NormativePopPlanningProblem haveFoodAndCoal_asProvider(){
        return new NormativePopPlanningProblem(
                SituationFactory. ,
                new ArrayList<>(),
                GoalFactory. ,
                OrganizationFactory.
        )
    }

    public static NormativePopPlanningProblem haveMoney_asExploiter(){

    }

    public static NormativePopPlanningProblem haveFoodCoalMoney_asProvider_asExploiter(){

    }
}
