package mock_logic.validation_model.norms;

import aStar_planning.pop_with_norms.Concept;
import aStar_planning.pop_with_norms.components.norms.Role;
import mock_logic.validation_model.AgentFactory;
import mock_logic.validation_model.Zones;
import mock_logic.validation_model.institutions.village.RoleFactory;
import aStar_planning.pop_with_norms.components.norms.ConstitutiveNorm;

import java.util.ArrayList;
import java.util.List;

public class ConstitutiveNormFactory {
    public static List<ConstitutiveNorm> exploitationOrganization(){
        List<ConstitutiveNorm> allConstitutiveNorms = new ArrayList<>();
        allConstitutiveNorms.addAll(zonesCountAsProtected());
        allConstitutiveNorms.addAll(zonesCountAsSacred());
        allConstitutiveNorms.add(agentCountAsFarmer());

        return allConstitutiveNorms;
    }

    private static ConstitutiveNorm countAs(Concept source, Role target){
        return new ConstitutiveNorm(source, target);
    }

    public static ConstitutiveNorm agentCountAsProvider() {
        return new ConstitutiveNorm(AgentFactory.SELF, RoleFactory.PROVIDER);
    }

    public static ConstitutiveNorm agentCountAsFarmer() {
        return new ConstitutiveNorm(AgentFactory.SELF, RoleFactory.FARMER);
    }

    public static List<ConstitutiveNorm> zonesCountAsSacred(){
        return new ArrayList<>(List.of(
            countAs(Zones.C1, RoleFactory.SACRED),
            countAs(Zones.D1, RoleFactory.SACRED),
            countAs(Zones.D2, RoleFactory.SACRED)
        ));
    }

    public static List<ConstitutiveNorm> zonesCountAsProtected(){
        return new ArrayList<>(List.of(
                countAs(Zones.A3, RoleFactory.PROTECTED),
                countAs(Zones.A4, RoleFactory.PROTECTED)
        ));
    }

    public static List<ConstitutiveNorm> householdOrganization(){
        List<ConstitutiveNorm> allConstitutiveNorms = new ArrayList<>();
        allConstitutiveNorms.add(agentCountAsProvider());

        return allConstitutiveNorms;
    }

}
