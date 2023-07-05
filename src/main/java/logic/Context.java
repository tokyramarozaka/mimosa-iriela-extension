package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A context is a technical implementation to differentiate variables of the same name, and
 * define their bindings. An id is provided to distinguish different contexts for readability
 */
@Getter
@EqualsAndHashCode
public class Context {
    private Integer id;
    private List<ContextPair> contextPairs;

    private final static AtomicInteger sequence = new AtomicInteger(0);

    public Context() {
        this.id = sequence.incrementAndGet();
        this.contextPairs = new ArrayList<>();
    }

    public Context(List<ContextPair> contextPairs){
        this.id = sequence.incrementAndGet();
        this.contextPairs = contextPairs;
    }
    public void unlink(Variable toUnlink){
        this.contextPairs
                .removeIf(contextPair -> contextPair.getVariable().equals(toUnlink));
    }
    public Context copy(){
        return new Context(new ArrayList<>(contextPairs));
    }

    public boolean isLinked(Variable variable) {
        return this.contextPairs
                .stream()
                .filter(pair -> pair.getVariable().equals(variable))
                .count() > 0;
    }

    public ContextualTerm getLink(Variable variable){
        return this.contextPairs
                .stream()
                .filter(pair -> pair.getVariable().equals(variable))
                .findFirst()
                .orElse(new ContextPair(variable, new ContextualTerm(this, variable)))
                .getContextualTerm();
    }

    public void link(Variable variable, Term to, Context toContext) {
        this.contextPairs.add(
                new ContextPair(variable, new ContextualTerm(toContext,to))
        );
    }

    public Unifiable build(Variable variable) {
        if(!this.isLinked(variable)){
            return variable;
        }

        ContextualTerm link = this.getLink(variable);

        if(link.getTerm() instanceof Variable){
            return link.getContext().build((Variable) link.getTerm());
        }

        return link.getTerm();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Context_")
                .append(this.id);

        this.contextPairs.forEach(pair -> stringBuilder.append("\t"+pair));

        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
