package graph;

import aStar.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class Link implements Operator {
    private Node from;
    private Node to;
    private Object content;

    public Link(Node from, Node to){
        this.from = from;
        this.to = to;
        this.content = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return from.equals(link.from) && to.equals(link.to) && Objects.equals(content,link.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, content);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "Link{" +
                "from=" + from.getName() +
                ", to=" + (to == null ? "" : to.getName()) +
                ", content=" + (this.content == null ? "" : content.toString()) +
                '}';
    }

    @Override
    public String toGraphArc() {
        return this.toString();
    }
}
