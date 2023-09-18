package mock_logic.validation_model.norms;

import mock_logic.validation_model.constants.Zones;
import mock_logic.validation_model.institutions.Village;
import aStar_planning.pop_with_norms.components.ConstitutiveNorm;

import java.util.ArrayList;
import java.util.List;

public class ConstitutiveNormFactory {
    public static List<ConstitutiveNorm> exploitationOrganization(){
        List<ConstitutiveNorm> allConstitutiveNorms = new ArrayList<>();
        allConstitutiveNorms.addAll(allProtectedZones());
        allConstitutiveNorms.addAll(allSacredZones());
        allConstitutiveNorms.add(agentCountAsFarmer());

        return allConstitutiveNorms;
    }

    public static ConstitutiveNorm agentCountAsProvider() {
        return new ConstitutiveNorm(AgentFactory.SELF, Village.PROVIDER);
    }

    public static ConstitutiveNorm agentCountAsFarmer() {
        return new ConstitutiveNorm(AgentFactory.SELF, Village.FARMER);
    }

    public static List<ConstitutiveNorm> allSacredZones(){
        return new ArrayList<>(List.of(
                zone(Zones.C1, Village.SACRED),
            countAs(Zones.D1, Village.SACRED),
            countAs(Zones.D2, Village.SACRED)
        ));
    }

    public static List<ConstitutiveNorm> allProtectedZones(){
        return new ArrayList<>(List.of(
                countAs(Zones.A3, Village._protected),
                countAs(Zones.A4, Village._protected)
        ));
    }

    public static List<ConstitutiveNorm> householdOrganization(){
        List<ConstitutiveNorm> allConstitutiveNorms = new ArrayList<>();
        allConstitutiveNorms.add(agentCountAsProvider());

        return allConstitutiveNorms;
    }

}
