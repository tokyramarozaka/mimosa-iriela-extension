package planning;

import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Situation {
    private List<Predicate> propositions;
}
