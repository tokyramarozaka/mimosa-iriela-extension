package graph.aStar_planning.pop.utils;

import constraints.PartialOrder;
import graph.aStar_planning.pop.utils.components.PopSituation;
import graph.aStar_planning.pop.utils.components.Step;
import constraints.TemporalConstraints;

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
