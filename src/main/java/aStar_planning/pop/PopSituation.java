package aStar_planning.pop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
@EqualsAndHashCode
public class PopSituation implements PartiallyOrderedElement {
    private int id;
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public PopSituation(){
        this.id = sequence.incrementAndGet();
    }
}
