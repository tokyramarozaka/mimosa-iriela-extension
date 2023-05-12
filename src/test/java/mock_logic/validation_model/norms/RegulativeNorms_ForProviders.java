package mock_logic.validation_model.norms;

import aStar_planning.pop_with_norms.components.norms.DeonticOperator;
import aStar_planning.pop_with_norms.components.norms.NormConditions;
import aStar_planning.pop_with_norms.components.norms.NormativeProposition;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import logic.Context;
import mock_logic.validation_model.AtomFactory;

import java.util.ArrayList;
import java.util.List;

public class RegulativeNorms_ForProviders {
    public static NormConditions obligationHaveFood_Conditions() {
        return new NormConditions(new ArrayList<>());
    }

    public static NormConsequences obligationHaveFood_Consequences() {
        return new NormativeProposition(new Context(), AtomFactory.haveFood());
    }

    public static RegulativeNorm obligation_haveFood() {
        return new RegulativeNorm(
                "must have food",
                DeonticOperator.OBLIGATION,
                obligationHaveFood_Conditions(),
                obligationHaveFood_Consequences()
        );
    }

    public static NormConditions obligation_haveWood_Conditions() {
        return new NormConditions(List.of(

        ));
    }

    public static NormConsequences obligation_haveWood_Consequences() {
        return new NormativeProposition(new Context(), AtomFactory.haveWood());
    }

    public static RegulativeNorm obligation_haveWood() {
        return new RegulativeNorm(
                "must have coal",
                DeonticOperator.OBLIGATION,
                obligation_haveWood_Conditions(),
                obligation_haveWood_Consequences()
        );
    }

    public static List<RegulativeNorm> providerNorms() {
        return List.of(obligation_haveFood(), obligation_haveWood());
    }
}
