package outputs;

import aStar.Operator;
import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
public class TotalOrderPlan implements PlanningOutput {
    private List<Operator> planActionInstances;

    @Override
    public String toString() {
        return planActionInstances.toString();
    }
}
