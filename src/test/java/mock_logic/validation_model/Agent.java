package mock_logic.validation_model;

import aStar_planning.pop_with_norms.Concept;
import logic.Constant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Agent implements Concept {
    private String name;

    @Override
    public String getName() {
        return "AGENT";
    }
}
