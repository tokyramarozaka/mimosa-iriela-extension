package aStar_planning.pop_with_norms.components;

import logic.Constant;
import logic.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
public class Role extends Constant{
    public Role(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
