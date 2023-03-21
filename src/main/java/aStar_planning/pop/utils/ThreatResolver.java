package aStar_planning.pop.utils;

import aStar.Operator;
import constraints.PartialOrder;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.TemporalConstraints;
import aStar_planning.pop.components.Threat;
import aStar_planning.pop.mapper.PlanModificationMapper;
import logic.Atom;
import constraints.Codenotation;
import constraints.CodenotationConstraints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes all the methods to resolve a threat in any given plan
 */
public class ThreatResolver {
    private Logger logger = LogManager.getLogger(ThreatResolver.class);
    /**
     * Return the list of all possible plan modifications to resolve a given threat by
     * putting the threatened step before the destroyer
     * @param plan : the plan we operate in
     * @param threat : the threat we are aiming to resolve
     * @return all the possible modifications which would resolve the threat.
     */
    public static List<Operator> byDestroyerDemotion(Plan plan, Threat threat) {
        List<Operator> planModifications = new ArrayList<>();

        PopSituation threatenedExitSituation = plan.getTc()
                .getFollowingSituation(threat.getThreatened());

        PopSituation destroyerEntrySituation = plan.getTc()
                .getPrecedingSituation(threat.getDestroyer());

        TemporalConstraints temporalChanges = new TemporalConstraints(List.of(
            new PartialOrder(threatenedExitSituation, destroyerEntrySituation)
        ));

        PlanModification destroyerDemotion = PlanModification
                .builder()
                .targetFlaw(threat)
                .addedTc(temporalChanges)
                .build();

        planModifications.add(destroyerDemotion);

        return planModifications;
    }

    /**
     * Resolve a threat by moving an existing step called "restablisher" between the destroyer and
     * the threatened step to regenerate the precondition again.
     * <br>
     * That restablisher cannot be the initial or final step as they cannot be moved in between
     * steps. Steps must also not be neither the destroyer nor the threatened step, as they cannot
     * be put before / after themselves.
     * @param plan : the plan in which we operate in
     * @param threat : the threat we are aiming to resolve
     * @return the set of plan modifications which would resolve the threat through a restablisher
     */
    public static List<Operator> byRestablishingStep(Plan plan, Threat threat){
        List<Operator> planModifications = new ArrayList<>();

        for(Step step : getOtherNonDummySteps(plan, threat.getDestroyer(), threat.getThreatened())){
            if (step.asserts(threat.getProposition(), plan.getCc())) {
                TemporalConstraints temporalChanges = tcToPutStepInBetween(
                        plan, step, threat.getDestroyer(), threat.getThreatened()
                );

                planModifications.add(PlanModificationMapper.from(threat, temporalChanges));
            }
        }

        return planModifications;
    }

    /**
     * Return the set of all possible plan modifications to resolve a threat by inserting a
     * non-codenotation constraint
     * @param plan : the plan to operate the potential modifications in
     * @param threat : the threat to resolve
     * @return the set of plan modifications which adds non codenotation constraints to solve the
     * threat
     */
    public static List<Operator> byNonCodenotation(Plan plan, Threat threat){
        List<Operator> planModifications = new ArrayList<>();
        CodenotationConstraints temp = plan.getCc().copy();

        CodenotationConstraints changes = new CodenotationConstraints();

        for (Atom consequence : threat.getDestroyer().getActionConsequences().getAtoms()) {
            if(consequence.isNegation() != threat.getProposition().getAtom().isNegation() &&
                    consequence.getPredicate().unify(
                    threat.getDestroyer().getActionInstance().getContext(),
                    threat.getProposition().getAtom().getPredicate(),
                    threat.getProposition().getContext(),
                    temp
            )){
                for (Codenotation addedCodenotation : temp.getCodenotations()) {
                    if (!plan.getCc().getCodenotations().contains(addedCodenotation)) {
                        changes.getCodenotations().add(addedCodenotation.reverse());
                    }
                }
            }
        }

        return Arrays.asList(PlanModificationMapper.from(threat, changes));
    }

    /**
     * Retrieves all steps within a plan except the two dummy steps at the beginning and at the end
     * while excluding a couple of steps as well.
     * @param plan : the plan in which we operate in
     * @param toExclude1 : one step to exclude
     * @param toExclude2 : another step to exclude
     * @return a list of all steps other than `initial`,`final` and the excluded steps
     */
    private static List<Step> getOtherNonDummySteps(Plan plan, Step toExclude1, Step toExclude2){
        return plan.getSteps().stream()
                .filter(step -> !step.isTheFinalStep() && !step.isTheInitialStep())
                .filter(step -> !step.equals(toExclude1)
                        && !step.equals(toExclude2))
                .collect(Collectors.toList());
    }

    /**
     * Builds the necessary temporal constraints to put a step in between two other steps
     * @param plan : the plan in which we operate
     * @param toPut : the step we want to add in between
     * @param leftStep : the step we want to happen before the step
     * @param rightStep : the step we want to happen after the step
     * @return the set of temporal constraints to put the step in between the left step and the
     * right step.
     */
    public static TemporalConstraints tcToPutStepInBetween(
            Plan plan,
            Step toPut,
            Step leftStep,
            Step rightStep
    ){
        PopSituation toPutEntry = plan.getTc().getPrecedingSituation(toPut);
        PopSituation toPutExit = plan.getTc().getFollowingSituation(toPut);

        PopSituation leftExit = plan.getTc().getFollowingSituation(leftStep);
        PopSituation rightEntry = plan.getTc().getPrecedingSituation(rightStep);

        return new TemporalConstraints(List.of(
                new PartialOrder(leftExit, toPutEntry),
                new PartialOrder(toPutExit, rightEntry)
        ));
    }

}
