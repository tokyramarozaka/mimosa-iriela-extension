package constraints;

import aStar_planning.pop.components.PlanElement;
import graph.Graph;
import graph.Link;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    public Link toLink(Graph graph){
        return new Link(
            graph.getContainingNode(this.firstElement), graph.getContainingNode(this.secondElement)
        );
    }
    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.firstElement)
                .append(" < ")
                .append(this.secondElement)
                .toString();
    }
}