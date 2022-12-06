package mini_mirana_IT.norms;


import norms.DeonticOperator;
import norms.Norm;
import norms.NormConditions;
import norms.NormConsequences;
import norms.RegulativeNorm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO : describe all regulative norms for the "Provider" role in Household
 */
public class RegulativeNorms_ForProvider {
    /**
     * TODO : write all norms preconditions and consequences
     * @return
     */
    private static NormConditions obligatedToProvideMeat_Conditions(){

    }

    private static NormConsequences obligatedToProvideMeat_Consequences(){

    }

    public static Norm obligatedToProvideMeat(){
        return new RegulativeNorm(
                "Obligated to provide meat",
                DeonticOperator.OBLIGATION,
                obligatedToProvideMeat_Conditions(),
                obligatedToProvideMeat_Consequences()
        );
    }

    private static NormConditions obligatedToProvideVegetables_Conditions(){

    }

    private static NormConsequences obligatedToProvideVegetables_Consequences(){

    }

    public static Norm obligatedToProvideVegetables(){
        return new RegulativeNorm(
                "Obligated to provide vegetables",
                DeonticOperator.OBLIGATION,
                obligatedToProvideVegetables_Conditions(),
                obligatedToProvideVegetables_Consequences()
        );
    }

    private static NormConditions obligatedToProvideWood_Conditions(){

    }

    private static NormConsequences obligatedToProvideWood_Consequences(){

    }

    public static Norm obligatedToProvideWood(){
        return new RegulativeNorm(
                "Obligated to provide wood",
                DeonticOperator.OBLIGATION,
                obligatedToProvideWood_Conditions(),
                obligatedToProvideWood_Consequences()
        );
    }

    public static List<Norm> allRegulativeNorms(){
        return new ArrayList<>(Arrays.asList(
                obligatedToProvideMeat(),
                obligatedToProvideWood(),
                obligatedToProvideVegetables()
        ));
    }
}
