package pop_IT.mock;

import aStar_planning.pop.components.Plan;

/**
 * TODO : A generator for mock plans for unit tests on Partial-Order Planning. Allows abstraction
 * on the tedious process of building some sample plans.
 */
public class MockPlan {
    public static Plan planWithInvalidCc(){
        return Plan.builder()
                .situations()
                .steps()
                .cc()
                .tc()
                .build();
    }

    public static Plan planWithInvalidTc(){
        return Plan.builder()
                .situations()
                .steps()
                .cc()
                .tc()
                .build();
    }

    public static Plan executablePlanWith_ABC_stacked() {
        return Plan.builder()
                .situations()
                .steps()
                .cc()
                .tc()
                .build();
    }

    public static Plan someInitialPlan() {
        return Plan.builder()
                .situations()
                .steps()
                .cc()
                .tc()
                .build();
    }
}
