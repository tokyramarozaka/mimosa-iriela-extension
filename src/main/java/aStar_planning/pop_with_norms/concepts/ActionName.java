package aStar_planning.pop_with_norms.concepts;

import logic.Action;
import logic.Constant;
import lombok.Getter;

@Getter
public class ActionName extends Constant {
    private Action action;

    public ActionName(String name, Action action) {
        super(name);
        this.action = action;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
