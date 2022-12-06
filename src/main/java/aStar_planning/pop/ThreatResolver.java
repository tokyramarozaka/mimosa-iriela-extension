package aStar_planning.pop;

import java.util.ArrayList;
import java.util.List;

public class ThreatResolver {
    public static List<PlanModification> byPromotion(Threat threat, Plan plan) {
        List<PlanModification> possibleSolutions = new ArrayList<>();

        PopSituation situationPostThreatened = plan
                .getTc()
                .getFollowingSituation(threat.getThreatened());

        PlanModification promotionOption = PlanModification.builder()
                .addedTc()
                .build();

        possibleSolutions.add(promotionOption);

        return possibleSolutions;
    }

    public static List<PlanModification> byDemotion(Threat threat, Plan plan) {

    }
}
