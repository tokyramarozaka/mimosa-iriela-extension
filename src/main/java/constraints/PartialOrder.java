package constraints;

import aStar_planning.pop.components.PlanElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A partial order between two elements describing that the first element should be before the
 * second element.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PartialOrder {
    private PlanElement firstElement;
    private PlanElement secondElement;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.firstElement)
                .append(" < ")
                .append(this.secondElement)
                .toString();
    }
}