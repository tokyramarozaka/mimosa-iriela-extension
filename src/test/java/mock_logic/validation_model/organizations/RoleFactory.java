package mock_logic.validation_model.organizations;

import aStar_planning.pop_with_norms.components.norms.Role;

/**
 * A class to provide all the basic roles present within the model
 */
public class RoleFactory {
    public static final Role PROVIDER = new Role("provider");
    public static final Role EXPLOITER = new Role("exploiter");
}