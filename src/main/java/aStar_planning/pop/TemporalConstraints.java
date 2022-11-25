package aStar_planning.pop;

import logic.Graphic;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TemporalConstraints extends Graphic {
    private List<PartialOrder> partialOrders;

    public boolean isCoherent() {
        return false;
    }

    public boolean isBefore(Step step, PlanElement element) {
        return false;
    }
}
