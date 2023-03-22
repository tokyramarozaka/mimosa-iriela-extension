package constraints;

import graph.aStar_planning.pop.utils.components.PlanElement;
import graph.Graph;
import graph.Link;
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
    private PlanElement before;
    private PlanElement after;

    public Link toLink(Graph graph){
        return new Link(
            graph.getContainingNode(this.before), graph.getContainingNode(this.after)
        );
    }
    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.before)
                .append(" < ")
                .append(this.after)
                .toString();
    }
}