package mock_logic.validation_model;

import logic.Context;
import logic.Goal;

import java.util.List;

public class GoalFactory {
    public static Goal haveFoodAndWood() {
        return new Goal(new Context(), List.of(
                AtomFactory.haveWood(),
                AtomFactory.haveFood()
        ));
    }

    public static Goal haveFoodWoodMoney() {
        return new Goal(new Context(), List.of(
                AtomFactory.haveWood(),
                AtomFactory.haveFood(),
                AtomFactory.haveMoney()
        ));
    }

    public static Goal haveMoney() {
        return new Goal(new Context(), List.of(
                AtomFactory.haveMoney()
        ));
    }
}
