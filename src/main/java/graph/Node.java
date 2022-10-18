package graph;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
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
}
