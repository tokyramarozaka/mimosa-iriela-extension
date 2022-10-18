package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A predicate with its variable bindings
 * @see logic.Predicate
 * @see logic.Context
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ContextualPredicate {
    private Context context;
    private Predicate predicate;

    public boolean isVerified(Situation toTest) {
        for (ContextualPredicate toTestContextualPredicate : toTest.getContextualPredicates()) {
            if(toTestContextualPredicate.getPredicate().unify(
                    toTestContextualPredicate.getContext(), this.getPredicate(),this.getContext()
            )){
                return true;
            }
        }
        return false;
    }
}