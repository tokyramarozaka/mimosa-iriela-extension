package graph;

import aStar.AStarResolver;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean pathExists(Node start, Node goal){
        try{
            AStarResolver search = new AStarResolver(new GraphForwardPlanningProblem(start,goal));
            search.findSolution();
        }catch (NoPlanFoundException exception){
            return false;
        }

        return true;
    }

    public boolean hasCycles(){
        for (Node node : this.nodes) {
            if(this.cyclicPathExists(node)){
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if there is a cycle within the temporal constraint from one node to itself.
     * @param node : the starting node
     * @return true if there is a path from one node to itself, false otherwise
     */
    private boolean cyclicPathExists(Node node) {
        try{
            AStarResolver search = new AStarResolver(new GraphForwardPlanningProblem(node, node));
            search.findSolution();
        }catch(NoPlanFoundException exception){
            return false;
        }

        return true;
    }

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
}
