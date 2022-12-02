package mini_mirana_IT.organizations;

import norms.Role;

/**
 * A class to provide all the basic roles present within the model
 */
public class RoleFactory {
    /**
     * GENERAL ROLES
     */
    public static final Role ECOLOGIST = new Role("ecologist");
    public static final Role PROVIDER = new Role("provider");
    public static final Role MEAT = new Role("meat");
    public static final Role PLANT = new Role("plant");

    /**
     * SPECIFIC ROLES
     */
    public static final Role BABAKOTO = new Role("babakoto");
    public static final Role TILAPIA = new Role("tilapia");
    public static final Role TISSAM = new Role("tissam");
    public static final Role BREDE_MAFANA = new Role("brede_mafana");
    public static final Role AGENT_1 = new Role("agent_1");
}
