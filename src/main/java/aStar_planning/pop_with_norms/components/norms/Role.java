package aStar_planning.pop_with_norms.components.norms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Role {
    private String name;

    @Override
    public String toString() {
        return "ROLE_"+this.name;
    }
}
