package mock_logic.validation_model.institutions.village;

import aStar_planning.pop_with_norms.components.norms.Role;
import logic.Constant;

/**
 * A class to provide all the basic roles present within the model
 */
public class RoleFactory {
    public static final Constant PROVIDER = new Constant("PROVIDER");
    public static final Constant FARMER = new Constant("FARMER");
    public static final Constant SACRED = new Constant("PROVIDER");
    public static final Constant PROTECTED = new Constant("PROTECTED");

    public static final Role Provider = new Role(PROVIDER);
    public static final Role Farmer = new Role(FARMER);
    public static final Role Sacred = new Role(SACRED);
    public static final Role Protected = new Role(PROTECTED);

}
