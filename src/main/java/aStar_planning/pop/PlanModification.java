package aStar_planning.pop;

import aStar.Operator;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlanModification implements Operator {
    private Flaw flaw;
    private Plan plan;

}
