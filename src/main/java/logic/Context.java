package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A context is a technical implementation to differentiate variables of the same name, and
 * define their bindings. An id is provided to distinguish different contexts for readability
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Context {
    private Integer id;
    private List<ContextPair> contextPairs;

    private final static AtomicInteger sequence = new AtomicInteger(0);


    public Context(List<ContextPair> contextPairs){
        this.id = sequence.incrementAndGet();
        this.contextPairs = contextPairs;
    }

    public Context() {
        this.id = sequence.incrementAndGet();
        this.contextPairs = new ArrayList<>();
    }

    public Context copy(){
        return new Context(id, contextPairs.stream().toList());
    }
}
