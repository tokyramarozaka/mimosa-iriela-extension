package mini_mirana_IT.norms;

import norms.DeonticOperator;
import norms.Norm;
import norms.NormConditions;
import norms.NormConsequences;
import norms.NormativeAction;
import norms.RegulativeNorm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO : Describe all regulative norms for the "Ecologist" role in Ecology
 */
public class RegulativeNorms_ForEcologist {
    private static NormConditions obligatedResourceGrowing_Conditions(){
        return new NormConditions();
    }

    private static NormConsequences obligatedResourceGrowing_Consequences(){
        return new NormativeAction();
    }

    public static Norm obligatedResourceGrowing(){
        return new RegulativeNorm(
                "Obligated resource growing",
                DeonticOperator.OBLIGATION,
                obligatedResourceGrowing_Conditions(),
                obligatedResourceGrowing_Consequences()
        );
    }

    public static List<Norm> allRegulativeNorms(){
        return new ArrayList<>(Arrays.asList(
                obligatedResourceGrowing()
        ));
    }
}