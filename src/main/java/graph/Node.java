package graph;

import aStar.AStarProblem;
import aStar.AStarResolver;
import aStar.State;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
public class Node implements State {
    private String name;
    private List<Link> links;
    private Object content;

    public Node(String name, List<Link> links) {
        super();
        this.name = name;
        this.links = links;
        this.content = null;
    }

    public void link(Node target){
        this.links.add(new Link(this, target, null));
    }

    public void link(Node target, Object content){
        this.links.add(new Link(this, target, content));
    }

    public boolean hasRecursiveLink(){
        AStarProblem searchCycleProblem;
        AStarResolver problemResolver;

        for (Link link : this.getLinks()) {
            searchCycleProblem = new GraphForwardPlanningProblem(link.getTo(), this);
            problemResolver = new AStarResolver(searchCycleProblem);

            try{
                problemResolver.findSolution();
                return true;
            }catch (NoPlanFoundException e) {
                return false;
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
