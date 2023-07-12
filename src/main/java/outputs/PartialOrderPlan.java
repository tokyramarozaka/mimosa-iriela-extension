package outputs;

import aStar_planning.pop.components.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PartialOrderPlan implements PlanningOutput {
    private Plan partialOrderPlan;

    @Override
    public String toString() {
        return this.partialOrderPlan.toString()
                .replaceAll("\\[", "[\n\t\t")
                .replaceAll(", ",",\n\t\t");
    }
}
