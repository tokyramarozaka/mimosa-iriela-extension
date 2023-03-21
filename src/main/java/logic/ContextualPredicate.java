package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A predicate with its variable bindings
 * @see logic.Predicate
 * @see logic.Context
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ContextualPredicate {
    private Context context;
    private Predicate predicate;
    private static final Logger logger = LogManager.getLogger("ContextualPredicate");

    public boolean isVerified(Situation toTest) {
        for (ContextualPredicate toTestContextualPredicate : toTest.getContextualPredicates()) {
            if(toTestContextualPredicate.getPredicate().unify(
                    toTestContextualPredicate.getContext().copy(),
                    this.getPredicate(),
                    this.getContext().copy()
            )){
                 return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.predicate)
                .append("\n")
                .append(this.context);

        return stringBuilder.toString();
    }
}