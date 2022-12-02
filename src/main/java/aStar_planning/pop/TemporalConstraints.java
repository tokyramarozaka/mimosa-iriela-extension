package aStar_planning.pop;

import graph.Node;
import logic.Graphic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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

    public void deleteRedundancies_onPlanModification(PlanModification changes){

    }

    public boolean isAfter(Step step, PopSituation situation) {
        return !isBefore(step, situation);
    }
}
