package mock_logic.validation_model;

import aStar_planning.pop_with_norms.concepts.Concept;
import lombok.Getter;

@Getter
public class Zone extends Term implements Concept {
    public Zone(String name) {
        super(name);
    }
}
