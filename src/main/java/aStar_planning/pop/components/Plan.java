package aStar_planning.pop.components;

import aStar.Operator;
import aStar.State;
import aStar_planning.pop.utils.OpenConditionResolver;
import aStar_planning.pop.utils.ThreatResolver;
import constraints.PartialOrder;
import constraints.TemporalConstraints;
import logic.Action;
import logic.Atom;
import constraints.CodenotationConstraints;
import logic.Context;
import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A plan is a sequence of situations, steps, codenotation constraints, and temporal constraints.
 * <ul>
 *     <li>Situations : discrete points of the plan arranged by a set of temporal constraints </li>
 *     <li>Steps : action instances which can be arranged through a set of temporal constraints</li>
 *     <li>Codenotation constraints : variable bindings of the plan</li>
 *     <li>Temporal constraints : arrangement of all steps and situations inside of the plan </li>
 * </ul>
 */
@Getter
@EqualsAndHashCode
public class Plan implements State {
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints cc;
    private TemporalConstraints tc;
    private Set<Flaw> flaws;
    private final static Logger logger = LogManager.getLogger(Plan.class);

    @Builder
    public Plan(List<PopSituation> situations, List<Step> steps, CodenotationConstraints cc,
                TemporalConstraints tc)
    {
        this.situations = situations;
        this.steps = steps;
        this.cc = cc;
        this.tc = tc;
        this.tc.updateGraph();
        this.evaluateFlaws();
    }

    private static boolean isNotFinalStep(Step step) {
        return !step.getActionInstance().getName().equals("final");
    }

    /**
     * (Re)computes all the flaws of the plan's steps : its open conditions and its threats
     */
    public void evaluateFlaws(){
        this.flaws = new HashSet<>();

        for(Step step : this.steps) {
            this.getThreats(step).forEach(threat -> this.flaws.add(threat));

            this.getOpenConditions(step).forEach(openCondition -> this.flaws.add(openCondition));
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
     * @param toSolve : the flaw which we want to solve
     * @return the list of plan modifications we can make to solve the given flaw.
     */
    private List<Operator> resolve(Flaw toSolve, List<Action> possibleActions){
        if(toSolve instanceof OpenCondition){
            return resolve((OpenCondition) toSolve, possibleActions);
        }else if(toSolve instanceof Threat){
            return resolve((Threat) toSolve);
        }

        throw new UnsupportedOperationException("This type of flaw is not implemented yet");
    }

    /**
     * Resolves an open condition using different strategies.
     * @param openCondition: the step precondition to be satisfied in its preceding step
     * @return the list of plan modifications which would solve the given open condition
     */
    private List<Operator> resolve(OpenCondition openCondition, List<Action> possibleActions) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(OpenConditionResolver.byPromotion(this, openCondition));
        resolvers.addAll(OpenConditionResolver.byCodenotation(this, openCondition));
        resolvers.addAll(OpenConditionResolver.byCreation(this,openCondition,possibleActions));

        return resolvers;
    }

    /**
     * Return all the plan modifications which resolves the given threat
     * @param threat : the threat to resolve describing which step is threatening which step's
     *               precondition
     * @return a set of plan modifications that remove the threat once applied.
     */
    private List<Operator> resolve(Threat threat){
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(ThreatResolver.byDestroyerDemotion(this, threat));
        resolvers.addAll(ThreatResolver.byNonCodenotation(this, threat));
        resolvers.addAll(ThreatResolver.byRestablishingStep(this, threat));

        return resolvers;
    }

    /**
     * Determines if a plan is executable : all step preconditions are necessarily true in
     * their preceding situation respectively (there is no open condition and no threat)
     * @return true if the plan is executable, i.e. has no more flaw, false otherwise
     */
    public boolean isExecutable() {
        return this.flaws.size() == 0;
    }

    /**
     * Checks if the current plan is coherent. A plan is coherent, iff :
     * <ul>
     *     <li>The set of codenotation constraints are coherent (not contradictory)</li>
     *     <li>The set of temporal constraints are coherent (not contradictory and not cyclic)</li>
     * </ul>
     * @return true if the plan is coherent, false otherwise.
     */
    public boolean isCoherent() {
        logger.info("CC COHERENCE CHECK : "+this.cc.isCoherent());
        logger.info("TC COHERENCE CHECK : "+this.tc.isCoherent());
        return this.cc.isCoherent() && this.tc.isCoherent();
    }

    /**
     * Apply a set of modifications on a plan, and returns the resulting plan
     * @param toApply : the plan modification to be applied
     * @return the plan resulting from the applied modifications.
     */
    public State applyPlanModification(Operator toApply) {
        return ((PlanModification)toApply).apply(this);
    }

    private boolean isInitialStep(Step step) {
        Action stepAction = ((Action) step.getActionInstance().getLogicalEntity());

        return Objects.equals(stepAction.getName(),"initial");
    }

    /**
     * Retrieves all the open conditions for a given step.
     * @param step : the step to analyze
     * @return all open conditions regarding a specific step
     */
    private List<Flaw> getOpenConditions(Step step) {
        List<Flaw> openConditions = new ArrayList<>();
        List<Atom> stepPreconditions = step.getActionPreconditions().getAtoms();

        for (Atom precondition : stepPreconditions) {
            ContextualAtom preconditionInstance = new ContextualAtom(
                    step.getActionInstance().getContext(), precondition
            );

            if(!isAsserted(preconditionInstance, tc.getPrecedingSituation(step))){
                openConditions.add(buildOpenCondition(precondition, step));
            }
        }

        return openConditions;
    }

    /**
     * TODO : Checks if a given proposition is necessarily true in a given situation
     * @param proposition : the proposition to check
     * @param situation : the situation where it needs to be checked.
     * @return true if the proposition is necessarily true in the given situation, false otherwise
     */
    public boolean isAsserted(ContextualAtom proposition, PopSituation situation) {
        for(Step establisher : getEstablishers(proposition, situation)){
            for(Step destroyer : getDestroyers(proposition, situation)){
                var postEstablisher = this.tc.getFollowingSituation(establisher);
                var postDestroyer = this.tc.getFollowingSituation((destroyer));

                if(this.isBefore(postEstablisher, postDestroyer)){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private List<Step> getDestroyers(ContextualAtom proposition, PopSituation situation) {
        return this.steps.stream()
                .filter(step -> !this.tc.isAfter(step, situation))
                .filter(step -> step.destroys(proposition, this.getCc()))
                .collect(Collectors.toList());
    }

    private List<Step> getEstablishers(ContextualAtom proposition, PopSituation situation) {
        return this.steps.stream()
                .filter(Plan::isNotFinalStep)
                .filter(step -> this.isBefore(this.tc.getFollowingSituation(step),situation))
                .filter(step -> step.asserts(proposition,this.getCc()))
                .collect(Collectors.toList());
    }

    /**
     * Builds an open condition based on which precondition is missing for a given step in its
     * preceding situation
     * @param missingPrecondition : the open condition which is not necessarily true
     * @param step : the bearer of the open condition
     * @return an open condition stating the missing precondition for which step
     */
    private OpenCondition buildOpenCondition(Atom missingPrecondition, Step step) {
        return new OpenCondition(
                tc.getPrecedingSituation(step),
                new ContextualAtom(step.getActionInstance().getContext(), missingPrecondition)
        );
    }

    /**
     * Build a threat describing what step threatens another step's precondition
     * @return a threat describing the destroyer, the threatened, and the destroyed precondition
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
    private List<Flaw> getThreats(Step toCheck) {
        return this.steps
                .stream()
                .filter(step -> this.isBefore(step, toCheck))
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

    /**
     * Checks if a given element is preceding the other element.
     * @param leftElement : the preceding element
     * @param rightElement : the next element
     * @return true if left element is before the right element.
     */
    public boolean isBefore(PlanElement leftElement, PlanElement rightElement){
        return this.tc.isBefore(leftElement,rightElement);
    }

    /**
     * TODO : Build a set of total order plan to execute using the partial-order of the plan
     * and its bindings
     * @return a sequence of action instance for the agent to execute.
     */
    public List<Operator> createInstance() {
        List<Operator> planActions = new ArrayList<>();

        this.steps.forEach(step -> {
            this.place(step, planActions);
        });

        return planActions;
    }

    /**
     * TODO : places the step into the set of actions with total order.
     * @param toPlace the step we want to place into the plan
     * @param toExecute : the list where we want to add the current plan
     */
    private void place(Step toPlace, List<Operator> toExecute){
        this.tc.getConcernedConstraints(toPlace).forEach(partialOrder -> {

        });
    }

    /**
     * TODO : determines if the element to add should be added at the start of the plan, in
     * between, or at the end of the plan
     */
    private void putOrShiftElement(Step toPlace, List<Operator> operators, PartialOrder partialOrder){
        // If the total ordered action set is empty, just insert it.
        if (operators.isEmpty()){
            operators.add(toPlace.getActionInstance());
            return;
        }

        // If the element to place is set to be before another element, put it before it, if any
        if (partialOrder.getFirstElement().equals(toPlace)){
            int otherIndex = operators.indexOf(partialOrder.getSecondElement());
            operators.add(otherIndex,toPlace.getActionInstance());
        }else if (partialOrder.getSecondElement().equals(toPlace)) {
            // If the element must be after a specific element, put it after it, if any.
            int otherIndex = operators.indexOf(partialOrder.getFirstElement());
            operators.add(otherIndex + 1, toPlace.getActionInstance());
        }
    }

    /**
     * Retrieves the initial step within the plan by using its reserved name.
     *
     * @see aStar_planning.pop.utils.PlanInitializer for more details on dummy steps naming
     * convention as "initial" and "final"
     *
     * @return a step whose name is `initial`.
     */
    public Step getInitialStep() {
        return this.steps.stream()
                .filter(step -> step.getActionInstance().getName().equals("initial"))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        String thisPlan = stringBuilder
                .append("PLAN\n")
                .append("--SITUATIONS\n")
                .append(this.situations)
                .append("\n--STEPS\n")
                .append(this.steps)
                .append("\n--CODENOTATIONS :\n")
                .append(this.cc)
                .append("\n--TEMPORAL CONSTRAINTS :\n")
                .append(this.tc)
                .append("\n--FLAWS : \n")
                .append(this.flaws)
                .toString();

        return thisPlan;
    }
}
