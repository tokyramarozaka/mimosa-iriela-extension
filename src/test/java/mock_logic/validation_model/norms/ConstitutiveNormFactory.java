package mock_logic.validation_model.norms;

import mock_logic.validation_model.organizations.RoleFactory;
import aStar_planning.pop_with_norms.components.norms.ConstitutiveNorm;
import settings.Keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describes all the constitutive norms of all organizations within the model, namely :
 * <ul>
 *     <li>some Ecology organization</li>
 *     <li>some Household organization</li>
 *     <li>some Northern organization</li>
 *     <li>some Southern organization</li>
 * </ul>
 */
public class ConstitutiveNormFactory {
    public static List<ConstitutiveNorm> agentCountAsProvider() {
        return new ArrayList<>(List.of(
            new ConstitutiveNorm(Keywords.AGENT_CONCEPT, RoleFactory.PROVIDER)
        ));
    }
}
