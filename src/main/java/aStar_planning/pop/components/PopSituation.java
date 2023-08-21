package aStar_planning.pop.components;

import graph.Node;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString
public class PopSituation implements PlanElement {
    private int id;
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public PopSituation(){
        this.id = sequence.incrementAndGet();
    }



    @Override
    public Node toNode() {
        return new Node(this.toString(), new ArrayList<>(), this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopSituation that = (PopSituation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
