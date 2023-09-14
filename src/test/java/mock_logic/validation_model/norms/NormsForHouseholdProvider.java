package mock_logic.validation_model.norms;

import aStar_planning.pop_with_norms.components.DeonticOperator;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.NormConditions;
import aStar_planning.pop_with_norms.components.NormConsequences;
import aStar_planning.pop_with_norms.components.NormativeProposition;
import aStar_planning.pop_with_norms.components.RegulativeNorm;
import logic.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NormsForHouseholdProvider {
    public static List<Norm> getAll() {
        return new ArrayList<>(Arrays.asList(
                providerMustHaveFood(),
                providerMustHaveWood()
        ));
    }

    private static Norm providerMustHaveFood(){
        return new RegulativeNorm(DeonticOperator.OBLIGATION, emptyPreconditions(), mustHaveFood());
    }
    private static Norm providerMustHaveWood(){
        return new RegulativeNorm(DeonticOperator.OBLIGATION,emptyPreconditions(), mustHaveWood());
    }
    private static NormConditions emptyPreconditions(){
        return new NormConditions();
    }
    private static NormConsequences mustHaveFood(){
        return new NormativeProposition(new Context(), AtomFactory.haveFood());
    }
    private static NormConsequences mustHaveWood(){
        return new NormativeProposition(new Context(), AtomFactory.haveWood());
    }
}
