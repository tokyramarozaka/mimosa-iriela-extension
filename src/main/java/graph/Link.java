package graph;

import aStar.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Link implements Operator {
    private Node from;
    private Node to;
    private Object content;
}
