package mini_mirana_IT.norms;

import aStar_planning.normative_pop.norms.DeonticOperator;
import aStar_planning.normative_pop.norms.Norm;
import aStar_planning.normative_pop.norms.NormConditions;
import aStar_planning.normative_pop.norms.NormConsequences;
import aStar_planning.normative_pop.norms.NormativeAction;
import aStar_planning.normative_pop.norms.RegulativeNorm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegulativeNorms_ForVillager {
    private static NormConditions prohibitedCutInProtectedZone_Conditions(){
        return new NormConditions();
    }

    private static NormConsequences prohibitedCutInProtectedZone_Consequences(){
        return new NormativeAction();
    }

    public static Norm prohibitedCutInProtectedZone(){
        return new RegulativeNorm(
                "Prohibited cut in protected zone",
                DeonticOperator.PROHIBITION,
                prohibitedCutInProtectedZone_Conditions(),
                prohibitedCutInProtectedZone_Consequences()
        );
    }
    private static NormConditions prohibitedTrespassingInSacredZone_Conditions(){
        return new NormConditions();
    }

    private static NormConsequences prohibitedTrespassingInSacredZone_Consequences(){
        return new NormativeAction();
    }

    public static Norm prohibitedTrespassingInSacredZone(){
        return new RegulativeNorm(
                "Prohibited trespassing in sacred zone",
                DeonticOperator.PROHIBITION,
                prohibitedTrespassingInSacredZone_Conditions(),
                prohibitedTrespassingInSacredZone_Consequences()
        );
    }

    private static NormConditions permittedHunt_ifLicense_Conditions(){

    }

    private static NormConsequences permittedHunt_ifLicense_Consequences(){

    }

    public static Norm permittedHunt_ifLicense(){
        return new RegulativeNorm(
                "Hunt permitted under license",
                DeonticOperator.PERMISSION,
                permittedHunt_ifLicense_Conditions(),
                permittedHunt_ifLicense_Consequences()
        );
    }

    private static NormConditions permittedCut_ifLicense_Conditions(){

    }

    private static NormConsequences permittedCut_ifLicense_Consequences(){

    }

    public static Norm permittedCut_ifLicense(){
        return new RegulativeNorm(
                "Cut permitted under license",
                DeonticOperator.PERMISSION,
                permittedCut_ifLicense_Conditions(),
                permittedCut_ifLicense_Consequences()
        );
    }

    public static List<Norm> allRegulativeNorms(){
        return new ArrayList<>(Arrays.asList(
            prohibitedCutInProtectedZone(),
            prohibitedTrespassingInSacredZone(),
            permittedHunt_ifLicense(),
            permittedCut_ifLicense()
        ));
    }
}
