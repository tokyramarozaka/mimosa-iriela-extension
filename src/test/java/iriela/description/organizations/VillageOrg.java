package iriela.description.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import iriela.description.constants.Agent;
import iriela.description.constants.Zones;
import logic.Predicate;
import iriela.description.institutions.Village;

import java.util.ArrayList;
import java.util.List;

public class VillageOrg {
    public static Organization get(){
        return new Organization(
                Village.get(),
                VillageOrg.constitutiveNorms(),
                VillageOrg.assertions()
        );
    }

    public static Organization get_withOnly_fishingNetPermission(){
        return new Organization(
          Village.get_fishingNetPermission(),
          VillageOrg.constitutiveNorms(),
          VillageOrg.assertions()
        );
    }
    private static List<Norm> constitutiveNorms() {
        return List.of(
                new ConstitutiveNorm(Agent.SELF, Village.member),

                new ConstitutiveNorm(Zones.A2, Village.sacred),
                new ConstitutiveNorm(Zones.B3, Village.sacred),
                new ConstitutiveNorm(Zones.C1, Village.sacred),

                new ConstitutiveNorm(Zones.A3, Village._protected),
                new ConstitutiveNorm(Zones.B1, Village._protected),

                new ConstitutiveNorm(Zones.B2, Village.office)
        );
    }

    private static List<Predicate> assertions() {
        return new ArrayList<>();
    }

    public static Organization get_withOnly_fishingReportObligation() {
        return new Organization(
                Village.get_fishingReportObligation(),
                VillageOrg.constitutiveNorms(),
                VillageOrg.assertions()
        );
    }

    public static Organization get_withOnly_trespassingProhibition() {
        return new Organization(
          Village.get_trespassingProhibition(),
          VillageOrg.constitutiveNorms(),
          VillageOrg.assertions()
        );
    }

    public static Organization get_withOnly_fishingWithoutLicenseProhibition() {
        return new Organization(
                Village.get_fishingWithoutLicenseProhibition(),
                VillageOrg.constitutiveNorms(),
                VillageOrg.assertions()
        );
    }

    public static Organization get_only_permittedCutIfLicense() {
        return new Organization(
                Village.get_permittedCutWithLicense(),
                VillageOrg.constitutiveNorms(),
                VillageOrg.assertions()
        );
    }
}
