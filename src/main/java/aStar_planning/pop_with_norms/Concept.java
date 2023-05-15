package aStar_planning.pop_with_norms;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Concept {
    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
