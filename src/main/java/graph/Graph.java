package graph;

import aStar.AStarResolver;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes;
    private List<Link> links;

    public Graph(List<Node> nodes){
        this.nodes = nodes;
        this.links = this.initializeLinks();
    }

    private List<Link> initializeLinks() {
        List<Link> links = new ArrayList<>();

        this.nodes.forEach(node -> links.addAll(node.getLinks()));

        return links;
    }

    public Node getContainingNode(Object content){
        return this.nodes
                .stream()
                .filter(node -> node.getContent().equals(content))
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
        return this.links.stream()
                .filter(link -> link.getTo().equals(node))
                .findFirst()
                .get()
                .getFrom();
    }

    public Node getFollowingNode(Node node) {
        return this.links.stream()
                .filter(link -> link.getFrom().equals(node))
                .findFirst()
                .get()
                .getTo();
    }
}
