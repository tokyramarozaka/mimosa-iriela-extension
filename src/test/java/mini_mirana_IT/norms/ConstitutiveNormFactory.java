package mini_mirana_IT.norms;

import mini_mirana_IT.organizations.RoleFactory;
import graph.aStar_planning.normative_pop.norms.ConstitutiveNorm;

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
    public static List<ConstitutiveNorm> someEcologyOrganization(){
        List<ConstitutiveNorm> constitutiveNorms = new ArrayList<>();

        constitutiveNorms.addAll(Arrays.asList(
            new ConstitutiveNorm(RoleFactory.AGENT_1, RoleFactory.ECOLOGIST),
            new ConstitutiveNorm(RoleFactory.TILAPIA, RoleFactory.MEAT),
            new ConstitutiveNorm(RoleFactory.TISSAM, RoleFactory.PLANT)
        ));

        return constitutiveNorms;
    }

    public static List<ConstitutiveNorm> someHouseholdOrganization() {
        List<ConstitutiveNorm> constitutiveNorms = new ArrayList<>();

        constitutiveNorms.addAll(Arrays.asList(
            new ConstitutiveNorm(RoleFactory.AGENT_1, RoleFactory.PROVIDER)
        ));

        return constitutiveNorms;
    }

    public static List<ConstitutiveNorm> someNorthernOrganization() {
        List<ConstitutiveNorm> constitutiveNorms = new ArrayList<>();

        constitutiveNorms.addAll(Arrays.asList(
                new ConstitutiveNorm(RoleFactory.BABAKOTO, RoleFactory.BABAKOTO)
        ));

        return constitutiveNorms;
    }

    public static List<ConstitutiveNorm> someSouthernOrganization() {
        List<ConstitutiveNorm> constitutiveNorms = new ArrayList<>();

        constitutiveNorms.addAll(Arrays.asList(
                new ConstitutiveNorm(RoleFactory.BREDE_MAFANA, RoleFactory.PLANT)
        ));

        return constitutiveNorms;
    }
}
