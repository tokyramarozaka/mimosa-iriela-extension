package aStar_planning.pop;

import java.util.List;

public class StepDetails {
//    public static List<Flaw> getOpenConditions(Step step, Plan plan){
//
//    }

//    public static List<Flaw> getThreats(Step step, Plan plan){
//        stepsBefore(step, plan)
//                .stream()
//                .filter(precedingStep -> precedingStep.destroys(step))
//                .map(FlawMapper.)
//    }

    public static List<Step> stepsBefore(PlanElement element, Plan plan){
        return plan.getSteps()
                .stream()
                .filter(step -> plan.getTemporalConstraints().isBefore(step,element))
                .toList();
    }

//    public static PopSituation getPrecedingSituation(Step step, Plan plan){
//
//    }
//
//    public static PopSituation getFollowingSituation(Step step, Plan plan){
//
//    }
}
