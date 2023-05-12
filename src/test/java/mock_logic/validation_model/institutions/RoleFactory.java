package mock_logic.validation_model.institutions;

import aStar_planning.pop_with_norms.components.norms.Role;

public class RoleFactory {
    public static Role providerRole() {
        return new Role("provider");
    }

    public static Role hunterRole() {
        return new Role("hunter");
    }
}