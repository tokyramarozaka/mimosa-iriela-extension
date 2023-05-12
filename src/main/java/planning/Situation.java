package planning;

import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * The external representation of a situation, from the user's point of view, masking any
 * implementation details such as contexts, atoms etc. At its core, a situation is simply a set of
 * propositions described by predicates, and nothing else.
 */
@AllArgsConstructor
@Getter
@ToString
public class Situation {
    private List<Predicate> propositions;
}
