package iriela.description;

import aStar_planning.pop_with_norms.NormativePlanningProblem;
import aStar_planning.pop_with_norms.components.Organization;
import logic.Action;
import logic.Goal;
import logic.Situation;
import iriela.description.organizations.GlobalOrg;
import iriela.description.organizations.HouseholdOrg;
import iriela.description.organizations.VillageOrg;

import java.util.ArrayList;
import java.util.List;

public class PlanningProblemFactory {
    public static NormativePlanningProblem irielaProblem(){
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<Action>(),
                new Goal(),
                new ArrayList<Organization>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get(),
                        HouseholdOrg.get()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_haveFishOnly(){
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get(),
                        HouseholdOrg.get_haveFishOnly()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_haveWoodOnly() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get(),
                        HouseholdOrg.get_haveWoodOnly()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_haveWoodOnly_noVillageNorms() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        //VillageOrg.get(),
                        HouseholdOrg.get_haveWoodOnly()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_haveFish_withRequiredLicenseForFishingNet() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get_withOnly_fishingNetPermission(),
                        HouseholdOrg.get_haveFishOnly()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_haveFish_withMandatoryFishReport() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get_withOnly_fishingReportObligation(),
                        HouseholdOrg.get_haveFishOnly()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_noTrespassing() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get_withOnly_trespassingProhibition(),
                        HouseholdOrg.get()
                ))
        );
    }

    public static NormativePlanningProblem irielaProblem_noFishingWithoutLicense() {
        return new NormativePlanningProblem(
                new Situation(),
                new ArrayList<>(),
                new Goal(),
                new ArrayList<>(List.of(
                        GlobalOrg.get(),
                        VillageOrg.get_withOnly_fishingWithoutLicenseProhibition(),
                        HouseholdOrg.get_haveFishOnly()
                ))
        );
    }
}
