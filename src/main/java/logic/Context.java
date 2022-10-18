package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * A context is a technical implementation to differentiate variables of the same name, and
 * define their bindings. An id is provided to distinguish different context for log readability
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Context {
    private int id;
    private List<ContextPair> contextPairs;

    public Context copy(){
        return new Context(id, contextPairs.stream().toList());
    }
}
