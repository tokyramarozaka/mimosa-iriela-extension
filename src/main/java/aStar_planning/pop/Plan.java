package aStar_planning.pop;

import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.Context;
import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Plan implements State {
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints codenotationConstraints;
    private TemporalConstraints temporalConstraints;
    private Set<Flaw> flaws;

    public Plan(List<PopSituation> situations, List<Step> steps,
                CodenotationConstraints codenotationConstraints,
                TemporalConstraints temporalConstraints) {
        this.situations = situations;
        this.steps = steps;
        this.codenotationConstraints = codenotationConstraints;
        this.temporalConstraints = temporalConstraints;
        this.flaws = new HashSet<>();
    }

    public void evaluateFlaws(){
        this.flaws = new HashSet<>();

        List<Step> stepsExceptInitialStep = this.steps
                .stream()
                .filter(step -> !isInitialStep(step))
                .toList();

        for(Step step : stepsExceptInitialStep) {
            this.getThreats(step)
                    .forEach(threat -> this.flaws.add(threat));

            this.getOpenConditions(step)
                    .forEach(openCondition -> this.flaws.add(openCondition));
        }
    }

    private boolean isInitialStep(Step step) {
        Action stepAction = ((Action) step.getActionInstance().getLogicalEntity());

        return Objects.equals(stepAction.getName(),"initial");
    }

    private List<Flaw> getOpenConditions(Step step) {
        List<Flaw> openConditions = new ArrayList<>();

        for (Atom precondition : step.getActionPreconditions().getAtoms()) {
            ContextualAtom preconditionInstance = new ContextualAtom(
                    step.getActionInstance().getContext(), precondition
            );

            if(!isAsserted(preconditionInstance,temporalConstraints.getPrecedingSituation(step))){
                openConditions.add(buildOpenCondition(precondition, step));
            }
        }

        return openConditions;
    }

    private boolean isAsserted(ContextualAtom proposition, PopSituation situation) {
        return false;
    }

    /**
     * Builds an open condition based on which precondition is missing for a given step in its
     * preceding situation
     * @param missingPrecondition
     * @param step
     * @return
     */
    private OpenCondition buildOpenCondition(Atom missingPrecondition, Step step) {
        return new OpenCondition(
                temporalConstraints.getPrecedingSituation(step),
                new ContextualAtom(step.getActionInstance().getContext(), missingPrecondition)
        );
    }

    /**
     * Build a threat describing which step threatens which step's precondition
     * @return
     */
    private Threat buildThreat(Step destroyer, Step threatened, ContextualAtom precondition){
        return new Threat(
                this.temporalConstraints.getPrecedingSituation(threatened),
                precondition,
                destroyer
        );
    }

    /**
     * Retrieves all the threats regarding a given step
     * @param toCheck : the step to check for threats
     * @return a list of all threats regarding the step to check.
     */
    private List<Flaw> getThreats(Step toCheck) {
        return this.steps
                .stream()
                .filter(step -> this.getTemporalConstraints().isBefore(step, toCheck))
                .filter(previousStep -> previousStep.isThreatening(toCheck))
                .map(threat -> buildThreat(
                        threat, toCheck, getThreatenedPrecondition(threat,toCheck)
                ))
                .collect(Collectors.toList());
    }

    private ContextualAtom getThreatenedPrecondition(Step threat, Step toCheck) {
        return null;
    }

    public List<Operator> possibleModifications() {
        List<Operator> possibleModifications = new ArrayList<>();

        //flaws.forEach(flaw -> possibleModifications.addAll(resolve(flaw)));

        return possibleModifications;
    }

    /**
     * TODO : A method to resolve a Flaw depending on its type : Open condition or Threat ?
     */
//    private List<Operator> resolve(Flaw flaw){
//        if(flaw instanceof OpenCondition){
//            return getAllResolvers((OpenCondition) flaw);
//        }
//
//        return getAllResolvers((Threat) flaw);
//    }

    /**
     * TODO
     * @param threat
     * @return
     */
//    private List<Operator> getAllResolvers(OpenCondition openCondition) {
//        List<Operator> resolvers = new ArrayList<>();
//
//        resolvers.addAll(OpenConditionResolver.resolversByPromotion());
//        resolvers.addAll(OpenConditionResolver.resolversByDemotion());
//        resolvers.addAll(OpenConditionResolver.resolversByCreation());
//        resolvers.addAll(OpenConditionResolver.resolversByCircumvention());
//
//        return resolvers;
//    }

    private List<Operator> getAllResolvers(Threat threat){
        List<Operator> resolvers = new ArrayList<>();

//        resolvers.addAll(ThreatResolver.resolversByPromotion(threat));
//        resolvers.addAll(ThreatResolver.resolversByDemotion(threat));
//        resolvers.addAll(ThreatResolver.resolversByCircumvention(threat));

        return resolvers;
    }

    /**
     * TODO: Determines if a plan is executable : all step preconditions are necessarily true in
     * their preceding situation respectively
     * @return true if the plan is executable, false otherwise
     */
    public boolean isExecutable() {
        return true;
    }

    public boolean isCoherent() {
        return this.codenotationConstraints.isCoherent() && this.temporalConstraints.isCoherent();
    }

    public State applyPlanModification(PlanModification operator) {
        return operator.apply(this);
    }
}
