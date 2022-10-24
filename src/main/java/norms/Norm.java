package norms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Norm {
    private String name;
    private DeonticOperator deonticOperator;
    private NormConditions normConditions;
    private NormConsequences normConsequences;
}
