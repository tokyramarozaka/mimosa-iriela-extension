package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Predicate implements Unifiable{
    private String name;
    private List<Term> terms;

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext) {
        return false;
    }

    @Override
    public boolean unify(Context currentContext, Unifiable other, Context otherContext, CodenotationConstraints codenotationConstraint) {
        return false;
    }

    @Override
    public Unifiable build(Context context) {
        return new Predicate(
                this.name,
                this.terms
                    .stream()
                    .map(term -> (Term) term.build(context))
                    .toList()
        );
    }
}
