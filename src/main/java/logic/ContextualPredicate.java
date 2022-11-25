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
    private static final Logger logger = LogManager.getLogger("Situation verification");

    public boolean isVerified(Situation toTest) {
        for (ContextualPredicate toTestContextualPredicate : toTest.getContextualPredicates()) {
            if(toTestContextualPredicate.getPredicate().unify(
                    toTestContextualPredicate.getContext(), this.getPredicate(),this.getContext()
            )){
                return true;
            }
        }

        logger.info("Not verified : "+this);
        return false;
    }

    @Override
    public String toString() {
        return this.predicate.build(this.context).toString();
    }
}