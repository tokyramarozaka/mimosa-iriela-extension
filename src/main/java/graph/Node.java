package graph;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
