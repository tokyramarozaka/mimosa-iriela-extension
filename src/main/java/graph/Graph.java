package graph;

import aStar.AStarPlanner;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Graph {
    private final List<Node> nodes;

    private final static Logger logger = LogManager.getLogger(Graph.class);

    public Graph(){
        this.nodes = new ArrayList<>();
    }

    public Graph(List<Node> nodes){
        this.nodes = nodes;
    }

    public Node getContainingNode(Object content){
        return this.nodes
                .stream()
                .filter(node -> node.getContent().toString().equals(content.toString()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a path exists from one node to another
     * @param start : the starting node
     * @param goal : the target node
     * @return true if there is a path between the two nodes and false otherwise
     */
    public boolean pathExists(Node start, Node goal){
        try{
            AStarPlanner search = new AStarPlanner(new GraphForwardPlanningProblem(start,goal));
            search.findSolution();
        }catch (NoPlanFoundException exception){
            return false;
        }

        return true;
    }

    public boolean longerPathExists(Node start, Node goal) {
        int solutionLength = 0;
        try{
            AStarPlanner search = new AStarPlanner(new GraphForwardPlanningProblem(start,goal));
            solutionLength = search.findSolution().size();
        }catch (NoPlanFoundException exception){
            return false;
        }

        return solutionLength > 1;
    }
    /**
     * Verifies if the graph has cycles meaning that you can go from one node and find your way
     * back. This is not desirable as it will lead to infinite loops in searches if the heuristic
     * is not good enough.
     * @return
     */
    public boolean hasCycles(){
        for (Node node : this.nodes) {
            if(node.hasRecursiveLink()){
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the node preceding the actual node in the parameter
     * @param node
     * @return
     */
    public Node getPrecedingNode(Node node) {
        return this.nodes.stream()
                .filter(precedingNode -> precedingNode.getLinks().stream()
                        .anyMatch(link -> link.getTo().equals(node)))
                .findAny()
                .get();
        }

    public Node getFollowingNode(Node node) {
        if (node.getLinks().size() == 0){
            return null;
        }
        return node.getLinks().get(0).getTo();
    }

    public List<Link> getLinks() {
        List<Link> links = new ArrayList<>();

        this.nodes.forEach(node -> {
            links.addAll(node.getLinks());
        });

        return links;
    }
}
