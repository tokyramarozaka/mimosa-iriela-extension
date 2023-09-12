package mock_logic.validation_model.institutions.global;

import aStar_planning.pop_with_norms.components.norms.Role;
import mock_logic.validation_model.institutions.global.ConstantFactory;

public class RoleFactory {
    public static Role MovingObject = new Role(ConstantFactory.MOVING_OBJECT);
    public static Role Zone = new Role(ConstantFactory.ZONE);
    public static Role Location = new Role(ConstantFactory.LOCATION);
}
