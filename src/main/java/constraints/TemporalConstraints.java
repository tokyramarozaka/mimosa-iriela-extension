package constraints;

import graph.aStar_planning.pop.utils.components.PlanElement;
import graph.aStar_planning.pop.utils.components.PlanModification;
import graph.aStar_planning.pop.utils.components.PopSituation;
import graph.aStar_planning.pop.utils.components.Step;
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
    private List<PartialOrder> partialOrders;

    public TemporalConstraints(){
        super();
        this.partialOrders = new ArrayList<>();
        updateGraph();
    }

    public TemporalConstraints(List<PartialOrder> partialOrders){
        super();
        this.partialOrders = partialOrders;
        updateGraph();
    }

    /**
     * Checks if the graph has no cycles which would cause an infinite loop
     * @return true if the graph has no cycles, and false otherwise.
     */
    public boolean isCoherent() {
        return !this.getGraph().hasCycles();
    }

    public boolean isBefore(PlanElement element1, PlanElement element2) {
        if(element1.equals(element2)){
            return false;
        }

        Node startingNode = this.getGraph().getContainingNode(element1);
        Node endingNode = this.getGraph().getContainingNode(element2);

        return this.getGraph().pathExists(startingNode, endingNode);
    }

    /**
     * Return the situation preceding a given step in the plan.
     * @param step : the step we want to check.
     * @return the single situation preceding the step
     */
    public PopSituation getPrecedingSituation(Step step){
        return (PopSituation) this.getGraph()
                .getPrecedingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }

    /**
     * Returns the situation following a given step in this temporal constraint using the graph
     * representation
     * @param step : the step whose preceding situation is wanted
     * @return the situation right before the step in the partial order.
     */
    public PopSituation getFollowingSituation(Step step){
        return (PopSituation) this.getGraph()
                .getFollowingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }

    /**
     * TODO : Remove all redundant constraints.
     * @param changes
     */
    public void deleteRedundancies_onPlanModification(PlanModification changes){

    }

    /**
     * Checks if an element (left) is after another element (right) according to the current
     * temporal constraints by using its graphical representation
     * @param left : the latter step which is after the right element
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
     * Returns the list of all partial orders which concerns a given step either the step being
     * on the left side or right side of the constraint.
     * @param target : the referred step
     * @return a list of partial order including the given step either on the left or the right
     */
    public List<PartialOrder> getConcernedConstraints(Step target) {
        List<PartialOrder> concernedConstraints = new ArrayList<>();

        for (PartialOrder partialOrder : this.partialOrders) {
            if (partialOrder.getBefore().equals(target) ||
                    partialOrder.getAfter().equals(target)) {
                        concernedConstraints.add(partialOrder);
            }
        }

        return concernedConstraints;
    }

    public void updateGraph(){
        List<Node> updatedNodes = this.initializeNodes();
        this.setGraph(new Graph(updatedNodes));
        this.initializeNodeLinks();
    }

    /**
     * (Re)Initializes all the nodes of this temporal constraints graphical representation.
     *
     * @return the list of all nodes to representing each partially ordered element
     */
    private List<Node> initializeNodes(){
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
     * TODO : find a better algorithm to generate all the links
     */
    private void initializeNodeLinks(){
        this.getGraph()
                .getNodes()
                .forEach(node -> node.getLinks().addAll(allLinkOfNode(node, this.getGraph())));
    }

    /**
     * Retrieves all the links related to the given node in the given graph
     * @param node : the target node we want to retrieve all links for
     * @param graph : the graph which contains all the nodes so that we can create links between
     *              existing nodes.
     * @return all links of a given node.
     */
    private List<Link> allLinkOfNode(Node node, Graph graph){

        return this.getPartialOrders()
                .stream()
                .filter(partialOrder -> partialOrder.getBefore().toNode().getName().equals(node.getName()))
                .map(partialOrder -> partialOrder.toLink(graph))
                .collect(Collectors.toList());
    }
}
