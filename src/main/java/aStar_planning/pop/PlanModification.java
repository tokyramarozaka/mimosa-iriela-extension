package aStar_planning.pop;

import aStar.Operator;
import aStar.State;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlanModification implements Operator {
    private Flaw flaw;
    private Plan plan;

    @Override
    public String getName() {
        return this.toString();
    }

    public Plan apply(Plan plan) {
        return null;
    }
}
