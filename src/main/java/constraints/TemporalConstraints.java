package constraints;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import graph.Graph;
import graph.Link;
import graph.Node;
import logic.Graphic;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class TemporalConstraints extends Graphic {
    private final List<PartialOrder> partialOrders;

    public TemporalConstraints() {
        super();
        this.partialOrders = new ArrayList<>();
        updateGraph();
    }

    public TemporalConstraints(List<PartialOrder> partialOrders) {
        super();
        this.partialOrders = partialOrders;
        updateGraph();
    }

    /**
     * Checks if the graph has no cycles which would cause an infinite loop
     *
     * @return true if the graph has no cycles, and false otherwise.
     */
    public boolean isCoherent() {
        return !this.getGraph().hasCycles();
    }

    public boolean isBefore(PlanElement element1, PlanElement element2) {
        if (element1.equals(element2)) {
            return false;
        }

        Node startingNode = this.getGraph().getContainingNode(element1);
        Node endingNode = this.getGraph().getContainingNode(element2);

        return this.getGraph().pathExists(startingNode, endingNode);
    }

    /**
     * Return the situation preceding a given step in the plan.
     *
     * @param step : the step we want to check.
     * @return the single situation preceding the step
     */
    public PopSituation getPrecedingSituation(Step step) {
        return (PopSituation) this.getGraph()
                .getPrecedingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }

    /**
     * Returns the situation following a given step in this temporal constraint using the graph
     * representation
     *
     * @param step : the step whose preceding situation is wanted
     * @return the situation right before the step in the partial order.
     */
    public PopSituation getFollowingSituation(Step step) {
        return (PopSituation) this.getGraph()
                .getFollowingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }


    /**
     * Checks if an element (left) is after another element (right) according to the current
     * temporal constraints by using its graphical representation
     * @param left  : the latter step which is after the right element
     * @param right : the former step which is before the left element
     * @return true if left > right temporally, and false otherwise
     */
    public boolean isAfter(PlanElement left, PlanElement right) {
        if (left.equals(right)) {
            return false;
        }

        return this.getGraph().pathExists(
                getGraph().getContainingNode(right),
                getGraph().getContainingNode(left)
        );
    }

    /**
     * Updates the graph to match the current temporal constraint.
     */
    public void updateGraph() {
        List<Node> updatedNodes = this.initializeNodes();
        this.setGraph(new Graph(updatedNodes));
        this.initializeNodeLinks();
    }

    /**
     * (Re)Initializes all the nodes of this temporal constraints graphical representation.
     *
     * @return the list of all nodes to representing each partially ordered element
     */
    private List<Node> initializeNodes() {
        List<Node> nodes = new ArrayList<>();

        this.partialOrders.forEach(temporalOrder -> {
            Node left = temporalOrder.getBefore().toNode();
            Node right = temporalOrder.getAfter().toNode();

            if (!nodes.contains(left)) nodes.add(left);

            if (!nodes.contains(right)) nodes.add(right);
        });

        return nodes;
    }

    /**
     * Initializes all the links of each node in the graph representation of the temporal constraint
     */
    private void initializeNodeLinks() {
        this.getGraph()
                .getNodes()
                .forEach(node -> node.getLinks().addAll(allLinkOfNode(node, this.getGraph())));
    }

    /**
     * Retrieves all the links related to the given node in the given graph
     * @param node  : the target node we want to retrieve all links for
     * @param graph : the graph containing all nodes and links
     * @return all links of a given node.
     */
    private List<Link> allLinkOfNode(Node node, Graph graph) {
        return this.getPartialOrders()
                .stream()
                .filter(partialOrder ->
                        partialOrder.getBefore().toNode().getName().equals(node.getName()))
                .map(partialOrder -> partialOrder.toLink(graph))
                .collect(Collectors.toList());
    }

    /**
     * <p>Refactor the temporal constraint by removing all the redundant partial orders using the
     * graph of all partial orders. A partial order <i>E(1) < E(N)</i> is <i>redundant</i> if there
     * exists within the plan a more explicit path from <i>E(1) to E(N)</i>.</p><br>
     *
     * <p>For example : <i>E(1) < E(2) < E(3) < ... < E(N)</i> is much more explicit than just
     * <i>E(1) < E(N)</i>, consequently we should delete the partial order <i>E(1) < E(N)</i>.</p>
     */
    public void refactorTemporalConstraints() {
        List<Link> toDelete = new ArrayList<>();

        for (Node node : this.getGraph().getNodes()) {
            for (Link link : node.getLinks()) {
                if(this.isRedundant(link, node)){
                    toDelete.add(link);
                }
            }
        }

        deletePartialOrdersIfLinkMatch(toDelete);
    }

    /**
     * Determines if a link is redundant within a node. It is redundant if there is another link
     * from the node, which will eventually lead to the same target. In this scenario, the longer
     * the path, the more explicit it is, the clearer the plan becomes. Longer path is better.
     * @param link : the link to check if it is redundant or not
     * @param node : the node which the link come from
     * @return true if link is redundant, and false otherwise.
     */
    private boolean isRedundant(Link link, Node node) {
        Node target = link.getTo();

        List<Link> otherLinks = node.getLinks()
                .stream()
                .filter(nodeLink -> !nodeLink.equals(link))
                .toList();

        for (Link otherLink : otherLinks) {
            if (this.getGraph().pathExists(otherLink.getTo(), target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes all partial orders from the current temporal constraints which matches the given set
     * of links in its graph, then updates the graph accordingly. As a reminder, both TemporalOrder
     * and Link is composed of two elements :
     * <ul>
     *     <li>A partial order has the before element, and the after element</li>
     *     <li>A link has a `from` node and a `to` node, which both contains elements similar to the
     *     elements of a partial order</li>
     * </ul>
     * @param toDelete : the list of all redundant links which needs to be removed from the graph
     */
    private void deletePartialOrdersIfLinkMatch(List<Link> toDelete) {
        toDelete.forEach(deletion -> this.partialOrders.removeIf(partialOrder ->
                partialOrder.getBefore().equals(deletion.getFrom().getContent())
                && partialOrder.getAfter().equals(deletion.getTo().getContent()))
        );

        this.updateGraph();
    }

    /**
     * Returns the element following a given situation, either it be another situation, or a step.
     * @param situation : the step we want to check for a following element
     * @return the element following a given situation
     * @throws NullPointerException if there is no following element to the situation
     */
    public PlanElement getFollowingElement(PopSituation situation) throws NullPointerException{
        var followingElement =  this.getGraph()
                .getFollowingNode(this.getGraph().getContainingNode(situation))
                .getContent();

        if(followingElement instanceof PopSituation){
            return (PopSituation) followingElement;
        }
        return (Step) followingElement;
    }
}

