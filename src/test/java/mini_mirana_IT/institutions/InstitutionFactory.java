package mini_mirana_IT.institutions;

import mini_mirana_IT.actions.EcologyActions;
import mini_mirana_IT.actions.HouseholdActions;
import mini_mirana_IT.actions.NorthernActions;
import mini_mirana_IT.actions.SouthernActions;
import mini_mirana_IT.actions.VillageActions;
import mini_mirana_IT.norms.RegulativeNorms_ForEcologist;
import mini_mirana_IT.norms.RegulativeNorms_ForNorthern;
import mini_mirana_IT.norms.RegulativeNorms_ForProvider;
import mini_mirana_IT.norms.RegulativeNorms_ForVillager;
import norms.Institution;
import norms.Norm;
import norms.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO : build these mocks
 */
public class InstitutionFactory {
    public static Institution ecologyInstitution(){
        Map<Role, List<Norm>> ecologyNorms = new HashMap<>(Map.of(
                new Role("ecologist"), RegulativeNorms_ForEcologist.allRegulativeNorms(),
                new Role("meat"), new ArrayList<>(),
                new Role("plant"), new ArrayList<>()
        ));

        return new Institution("Ecology", ecologyNorms, EcologyActions.allActions());
    }

    public static Institution householdInstitution(){
        Map<Role, List<Norm>> householdNorms = new HashMap<>(Map.of(
                new Role("provider"), RegulativeNorms_ForProvider.allRegulativeNorms()
        ));

        return new Institution("Household", householdNorms, HouseholdActions.allActions());
    }

    public static Institution villageInstitution(){
        Map<Role, List<Norm>> villageNorms = new HashMap<>(Map.of(
                new Role("villager"), RegulativeNorms_ForVillager.allRegulativeNorms()
        ));

        return new Institution("Village", villageNorms, VillageActions.allActions());
    }

    public static Institution northernInstitution(){
        Map<Role, List<Norm>> northernNorms = new HashMap<>(Map.of(
                new Role("nothern"), RegulativeNorms_ForNorthern.allRegulativeNorms()
        ));

        return new Institution("North", northernNorms, NorthernActions.allActions());
    }

    public static Institution southernInstitution(){
        Map<Role, List<Norm>> southernEndowedNorms = new HashMap<>(Map.of(
                new Role("southern"), new ArrayList<>()
        ));

        return new Institution("South", southernEndowedNorms, SouthernActions.allActions());
    }
}
