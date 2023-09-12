package mock_logic.validation_model.institutions.village;

import aStar_planning.pop_with_norms.components.norms.Role;
import logic.Constant;

/**
 * A class to provide all the basic roles present within the model
 */
public class RoleFactory {
    public static final Role Sacred = new Role(ConstantFactory.SACRED);
    public static final Role Protected = new Role(ConstantFactory.PROTECTED);

}
