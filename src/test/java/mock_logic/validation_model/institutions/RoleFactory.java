package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.components.norms.Role;

/**
 * A class to provide all the basic roles present within the model
 */
public class RoleFactory {
    public static final Role PROVIDER = new Role("PROVIDER");
    public static final Role FARMER = new Role("FARMER");
    public static final Role SACRED = new Role("SACRED");
    public static final Role PROTECTED = new Role("PROTECTED");
}
