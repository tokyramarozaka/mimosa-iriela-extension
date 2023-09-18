package aStar_planning.pop_with_norms.concepts;

import logic.Action;
import logic.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ActionName extends Constant {
    public ActionName(String name) {
        super(name);
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
