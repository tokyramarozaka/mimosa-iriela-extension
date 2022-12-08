package aStar_planning.pop.utils;

import aStar_planning.pop.components.PartialOrder;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.TemporalConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemporalConstraintsBuilder {
    public static TemporalConstraints tcForStepInsertion(
            Step addedStep,
            PopSituation leftEdge,
            PopSituation entrySituation,
            PopSituation exitSituation,
            PopSituation rightEdge
    ){
        List<PartialOrder> toAdd = new ArrayList<>();

        toAdd.add(new PartialOrder(leftEdge, entrySituation));
        toAdd.add(new PartialOrder(entrySituation, addedStep));
        toAdd.add(new PartialOrder(addedStep, exitSituation));
        toAdd.add(new PartialOrder(exitSituation, rightEdge));

        return new TemporalConstraints(toAdd);
    }

    public static List<PartialOrder> placeBefore(PopSituation toPlace, PopSituation situation) {
        return Arrays.asList(new PartialOrder(toPlace, situation));
    }
}
