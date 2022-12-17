package constraints;

import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import graph.Node;
import logic.Graphic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class TemporalConstraints extends Graphic {
    private List<PartialOrder> partialOrders;

    public boolean isCoherent() {
        return !this.getGraph().hasCycles();
    }

    public boolean isBefore(PlanElement element1, PlanElement element2) {
        if(element1.equals(element2)){
            return false;
        }

        Node startingNode = this.getGraph().getContainingNode(element1);
        Node endingNode = this.getGraph().getContainingNode(element2);

        return this.getGraph().pathExists(startingNode, endingNode);
    }

    public PopSituation getPrecedingSituation(Step step){
        return (PopSituation) this.getGraph()
                .getPrecedingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }

    public PopSituation getFollowingSituation(Step step){
        return (PopSituation) this.getGraph()
                .getFollowingNode(this.getGraph().getContainingNode(step))
                .getContent();
    }

    /**
     * TODO : Remove all redundant constraints.
     * @param changes
     */
    public void deleteRedundancies_onPlanModification(PlanModification changes){

    }

    public boolean isAfter(Step step, PopSituation situation) {
        return !isBefore(step, situation);
    }

    public List<PartialOrder> getConcernedConstraints(Step toPlace) {
        List<PartialOrder> concernedConstraints = new ArrayList<>();

        for (PartialOrder partialOrder : this.partialOrders) {
            if (partialOrder.getFirstElement().equals(toPlace) ||
                    partialOrder.getSecondElement().equals(toPlace)) {
                        concernedConstraints.add(partialOrder);
            }
        }

        return concernedConstraints;
    }
}
