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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class Plan implements State {
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints cc;
    private TemporalConstraints tc;
    private Set<Flaw> flaws;

    public Plan(List<PopSituation> situations, List<Step> steps,
                CodenotationConstraints cc,
                TemporalConstraints tc) {
        this.situations = situations;
        this.steps = steps;
        this.cc = cc;
        this.tc = tc;
        this.evaluateFlaws();
    }

    /**
     * (Re)computes all the flaws of the plan : its open conditions and its threats
     */
    public void evaluateFlaws(){
        this.flaws = new HashSet<>();

        for(Step step : this.steps) {
            this.getStepThreats(step)
                    .forEach(threat -> this.flaws.add(threat));

            this.getOpenConditions(step)
                    .forEach(openCondition -> this.flaws.add(openCondition));
        }
    }

    /**
     * Computes the set of all possible modifications all plan flaws
     * @return a List of all possible Plan modifications to solve all flaws in the flaw list
     */
    public List<Operator> allPossibleModifications(List<Action> possibleActions) {
        List<Operator> possibleModifications = new ArrayList<>();

        flaws.forEach(flaw -> possibleModifications.addAll(resolve(flaw, possibleActions)));

        return possibleModifications;
    }

    /**
     * Resolves a flaw depending on its type. A list of possible actions is required to resolve
     * open conditions as some action instances might be created.
     * @param flaw
     * @return
     */
    private List<Operator> resolve(Flaw flaw, List<Action> possibleActions){
        if(flaw instanceof OpenCondition){
            return resolve((OpenCondition) flaw, possibleActions);
        }else if(flaw instanceof Threat){
            return resolve((Threat) flaw);
        }

        throw new UnsupportedOperationException("This type of flaw is not implemented yet");
    }

    /**
     * Resolves an open condition
     * @param openCondition: the step precondition to be satisfied in its preceding step
     * @return the list of plan modifications which would remove the given open condition
     */
    private List<Operator> resolve(OpenCondition openCondition, List<Action> possibleActions) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(OpenConditionResolver.byDemotion(openCondition, this));
        resolvers.addAll(OpenConditionResolver.byCodenotation(this, openCondition));
        resolvers.addAll(OpenConditionResolver.byCreation(this,openCondition, possibleActions)
        );

        return resolvers;
    }

    private List<Operator> resolve(Threat threat){
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(ThreatResolver.byPromotion(threat, this));
        resolvers.addAll(ThreatResolver.byDemotion(threat, this));

        return resolvers;
    }

    /**
     * Determines if a plan is executable : all step preconditions are necessarily true in
     * their preceding situation respectively (there is no open condition and no threat)
     * @return true if the plan is executable, false otherwise
     */
    public boolean isExecutable() {
        return this.flaws.size() == 0;
    }

    public boolean isCoherent() {
        return this.cc.isCoherent() && this.tc.isCoherent();
    }

    public State applyPlanModification(PlanModification operator) {
        return operator.apply(this);
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

            if(!isAsserted(preconditionInstance, tc.getPrecedingSituation(step))){
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
     * @param missingPrecondition : the open condition which is not necessarily true
     * @param step : the bearer of the open condition
     * @return
     */
    private OpenCondition buildOpenCondition(Atom missingPrecondition, Step step) {
        return new OpenCondition(
                tc.getPrecedingSituation(step),
                new ContextualAtom(step.getActionInstance().getContext(), missingPrecondition)
        );
    }

    /**
     * Build a threat describing what step threatens antoher step's precondition
     * @return
     */
    private Threat buildThreat(Step destroyer, Step threatened, ContextualAtom precondition){
        return new Threat(
                destroyer,
                threatened,
                this.tc.getPrecedingSituation(threatened),
                precondition
        );
    }

    /**
     * Retrieves all the threats regarding a given step
     * @param toCheck : the step to check for threats
     * @return a list of all threats regarding the step to check.
     */
    private List<Flaw> getStepThreats(Step toCheck) {
        return this.steps
                .stream()
                .filter(step -> this.getTc().isBefore(step, toCheck))
                .filter(previousStep -> previousStep.isThreatening(toCheck))
                .map(threat -> buildThreat(
                        threat, toCheck, getThreatenedPrecondition(threat,toCheck)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves which precondition is threatened between the threatener and the threatened step
     * @param threat : the threatening step which destroys the precondition
     * @param threatened : the step bearing the threatened precondition
     * @return the threatened proposition
     */
    private ContextualAtom getThreatenedPrecondition(Step threat, Step threatened) {
        Context threatenedContext = threatened.getActionInstance().getContext();

        for (Atom precondition : threatened.getActionPreconditions().getAtoms()) {
            if(threat.destroys(threatenedContext, precondition, this.getCc())){
                return new ContextualAtom(threatened.getActionInstance().getContext(),precondition);
            }
        }

        return null;
    }

    public List<Step> searchSolvingStep(OpenCondition openCondition, List<Action> possibleActions) {
        List<Step> solvingSteps = new ArrayList<>();

        possibleActions.forEach(possibleAction -> {
            solvingSteps.addAll(
                possibleSolvingInstances(possibleAction, openCondition.getProposition())
            );
        });

        return solvingSteps;
    }
}
