package mock_logic.validation_model;

import aStar_planning.pop_with_norms.NormativePopPlanningProblem;
import mock_logic.validation_model.organizations.OrganizationFactory;

import java.util.ArrayList;

public class PlanningProblemFactory {
    public static NormativePopPlanningProblem haveFoodAndWood_asProvider(){
        return new NormativePopPlanningProblem(
                SituationFactory.mapInitialSituationToContext(),
                new ArrayList<>(),
                GoalFactory.haveFoodAndWood() ,
                OrganizationFactory.agentCountAsProvider_inHousehold()
        );
    }

    public static NormativePopPlanningProblem haveMoney_asExploiter(){
        return new NormativePopPlanningProblem(
                SituationFactory.mapInitialSituationToContext(),
                new ArrayList<>(),
                GoalFactory.haveMoney(),
                OrganizationFactory.agentCountAsExploiter_inExploitation()
        );
    }

    public static NormativePopPlanningProblem haveFoodCoalMoney_asProvider_asExploiter(){
        return new NormativePopPlanningProblem(
                SituationFactory.mapInitialSituationToContext(),
                new ArrayList<>(),
                GoalFactory.haveFoodWoodMoney(),
                OrganizationFactory.agentCountAs_providerAndExploiter()
        );
    }
}
