package aStar_planning.pop.components;

import aStar_planning.pop.components.PlanElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
@EqualsAndHashCode
public class PopSituation implements PlanElement {
    private int id;
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public PopSituation(){
        this.id = sequence.incrementAndGet();
    }
}
