package constraints;

import aStar.AStarResolver;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PlanModification;
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
    private List<PartialOrder> partialOrders;

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
     * TODO : Remove all redundant constraints.
     *
     * @param changes
     */
    public void deleteRedundancies_onPlanModification(PlanModification changes) {

    }

    /**
     * Checks if an element (left) is after another element (right) according to the current
     * temporal constraints by using its graphical representation
     *
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
     * Returns the list of all partial orders which concerns a given step either the step being
     * on the left side or right side of the constraint.
     *
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
     * TODO : find a better algorithm to generate all the links
     */
    private void initializeNodeLinks() {
        this.getGraph()
                .getNodes()
                .forEach(node -> node.getLinks().addAll(allLinkOfNode(node, this.getGraph())));
    }

    /**
     * Retrieves all the links related to the given node in the given graph
     *
     * @param node  : the target node we want to retrieve all links for
     * @param graph : the graph which contains all the nodes so that we can create links between
     *              existing nodes.
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
     * Deletes all redundant temporal orders from the plan. A redundancy can be defined as follows :
     * if A < B, and B < C, then you don't need to state that A < C anymore since it can de deducted
     * @param addedStep : the potentially added step (can be null)
     * @param addedTc : the added temporal constraint when modifying the plan (can be null also)
     * @param oldTc : the current temporal constraint where we will delete redundancies
     */
    public void deleteRedundancies(
            Step addedStep,
            TemporalConstraints addedTc,
            TemporalConstraints oldTc
    ){
        // If we don't add any new step then check if there is a new Temporal constraint
        if (addedStep == null) {
            // If there is no new temporal constraint there is no new redundancy at all
            if (addedTc == null) {
                return;
            }

            for (PartialOrder temporalOrder : addedTc.getPartialOrders()) {
                if(oldTc.getGraph().getContainingNode(temporalOrder.getAfter()) == null){
                    System.out.println(temporalOrder.getAfter() + " is not in the graph.");
                    System.out.println("TC : "+this);
                    System.out.println("Graph is " + oldTc.getGraph());
                }
                // We retrieve both edges of the temporal order (both are Situations, no step added)
                PopSituation target = (PopSituation) oldTc.getGraph()
                        .getContainingNode(temporalOrder.getAfter())
                        .getContent();

                PopSituation source = (PopSituation) oldTc.getGraph()
                        .getContainingNode(temporalOrder.getBefore())
                        .getContent();

                // We remove any previous direct link which could have happened beforehand
                // Because this one is more explicit than the previous one
                deletePreviousRedundancies(source, target, temporalOrder, oldTc);

                // We remove any direct link which could have happened after the temporal order
                deleteFollowingRedundancies(source, target, temporalOrder, oldTc);
            }
        } else {
            // If we added a new step we check for redundancies in the span where it is being added
            deleteRedundanciesFromNewStep(addedStep, addedTc, oldTc);
        }
    }


    /**
     * Deletes all redundance before the addition of the temporal constraint : we verify that there
     * is no temporal order before the left element which has become redundant. For instance : we
     * have A < B, and we add C < B, if we can prove that A < C, then A < B is redundant
     *
     * @param source
     * @param target
     * @param temporalOrder
     * @param oldTc
     */
    private void deletePreviousRedundancies(
            PopSituation source,
            PopSituation target,
            PartialOrder temporalOrder,
            TemporalConstraints oldTc
    ) {
        // We are going to verify all the OTHER links that leads to that target element
        oldTc.getGraph().getLinks()
                .stream()
                .filter(link -> link.getTo().getContent().equals(target))
                .forEach(linkToTarget -> {
                    // Do they have a path to the source element (which leads to the target element by the way)
                    AStarResolver searchInChain = new AStarResolver(
                            new GraphForwardPlanningProblem(
                                    linkToTarget.getFrom(),
                                    oldTc.getGraph().getContainingNode(source))
                    );

                    try {
                        searchInChain.findSolution();

                        // If yes, then delete it, we don't need it anymore, as we have the source as intermediate to the target
                        this.getPartialOrders().removeIf(t ->
                                t.getBefore().equals(linkToTarget.getFrom().getContent())
                                        && t.getAfter().equals(linkToTarget.getTo().getContent())
                        );

                        this.updateGraph();
                    } catch (Exception e) {
                        // If there is no direct link : do nothing
                    }
                });
    }

    /**
     * Deletes the redundances after adding a new temporal constraint. This deletes every redundant
     * link after the added temporal order. For instance : If we have A < B, and we added A < C,
     * then A to B is redundant if we have C < B (B is reachable from C)
     *
     * @param source
     * @param target
     * @param temporalOrder
     * @param oldTc
     */
    private void deleteFollowingRedundancies(
            PopSituation source,
            PopSituation target,
            PartialOrder temporalOrder,
            TemporalConstraints oldTc
    ) {
        // We are going to verify all the links from the source of the temporal order
        // to see if they are redundant
        oldTc.getGraph().getLinks()
                .stream()
                .filter(link -> link.getFrom().getContent().equals(source))
                .forEach(linkFromSource -> {
                    // Is the new temporal order reaching the element which the source is linked to ?
                    AStarResolver searchInChain = new AStarResolver(
                            new GraphForwardPlanningProblem(oldTc.getGraph().getContainingNode(target), oldTc.getGraph().getContainingNode(linkFromSource.getTo())));

                    try {
                        searchInChain.findSolution();

                        // If yes, then delete it, we don't need it anymore, as we have the target as intermediate
                        this.getPartialOrders().removeIf(t ->
                                t.getBefore().equals(oldTc.getGraph().getContainingNode(source).getContent())
                                        && t.getAfter().equals(linkFromSource.getTo().getContent()));
                        this.updateGraph();
                    } catch (Exception e) {
                        // If there is no direct link : do nothing
                    }
                });
    }

    /**
     * Delete all redundances from adding a new step : this deletes any direct link between
     * the two situations at both ends of the new step (preceding its entry situation, and after its exit situation)
     *
     * @param addedStep
     * @param addedTc
     * @param oldTc
     */
    private void deleteRedundanciesFromNewStep(
            Step addedStep,
            TemporalConstraints addedTc,
            TemporalConstraints oldTc
    ) {
        // Let's take back all the situations around the step
        PopSituation beforeStep = addedTc.getPrecedingSituation(addedStep);
        PopSituation afterStep = addedTc.getFollowingSituation(addedStep);

        // Check the boundaries in between which the step has been inserted
        PopSituation leftBoundary = (PopSituation) addedTc.getPartialOrders()
                .stream()
                .filter(temporalOrder -> temporalOrder.getAfter().equals(beforeStep))
                .findFirst()
                .get()
                .getBefore();

        PopSituation rightBoundary = (PopSituation) addedTc.getPartialOrders()
                .stream()
                .filter(temporalOrder -> temporalOrder.getBefore().equals(afterStep))
                .findFirst()
                .get()
                .getAfter();

        AStarResolver searchDirectLink = new AStarResolver(
                new GraphForwardPlanningProblem(
                        oldTc.getGraph().getContainingNode(leftBoundary),
                        oldTc.getGraph().getContainingNode(rightBoundary)
                )
        );

        // Try to find a direct link between the boundaries if there is any
        try {
            searchDirectLink.findSolution();

            // Remove the direct link if there is any
            this.getPartialOrders().removeIf(t -> t.getBefore().equals(leftBoundary)
                    && t.getAfter().equals(rightBoundary));

            // Update the graph accordingly
            this.updateGraph();
        } catch (Exception e) {
            // If there is no direct path : Do nothing
        }
    }
}
