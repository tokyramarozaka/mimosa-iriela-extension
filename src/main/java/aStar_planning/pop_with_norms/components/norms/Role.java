package aStar_planning.pop_with_norms.components.norms;

import logic.Constant;
import logic.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Role extends Constant{
    public Role(String name) {
        super(name);
    }
}
