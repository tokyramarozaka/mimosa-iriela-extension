package mock_logic.validation_model;

import aStar_planning.pop_with_norms.Concept;
import logic.Constant;
import lombok.Getter;

@Getter
public class Zone extends Term implements Concept {
    public Zone(String name) {
        super(name);
    }
}
