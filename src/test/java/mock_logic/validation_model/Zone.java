package mock_logic.validation_model;

import aStar_planning.pop_with_norms.Concept;
import logic.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Zone extends Constant implements Concept {
    public Zone(String name) {
        super(name);
    }
}
