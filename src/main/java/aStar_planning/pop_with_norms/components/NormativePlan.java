package aStar_planning.pop_with_norms.components;

import aStar.Operator;
import aStar.State;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.Threat;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.resolvers.MissingObligationResolver;
import aStar_planning.pop_with_norms.resolvers.MissingProhibitionResolver;
import aStar_planning.pop_with_norms.utils.NormsPerInterval;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import exception.UnapplicableNormException;
import logic.Action;
import logic.ContextualAtom;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NormativePlan extends Plan {
    private final static Logger logger = LogManager.getLogger(NormativePlan.class);
    private final List<Organization> organizations;
    private final List<NormsPerInterval> normsPerIntervals;
    private List<Organization> activeOrganizations;

    public NormativePlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations,
            List<NormsPerInterval> normsPerIntervals
    ) {
        super(situations, steps, cc, tc);
        this.organizations = organizations;
        this.evaluateActiveOrganizations();
        this.normsPerIntervals = normsPerIntervals;
        this.evaluateNormativeFlaws();
    }

    public NormativePlan(
            Plan plan,
            List<Organization> organizations,
            List<NormsPerInterval> normsPerIntervals
    ) {
        super(plan.getSituations(), plan.getSteps(), plan.getCc(), plan.getTc());
        this.organizations = organizations;
        this.evaluateActiveOrganizations();
        this.normsPerIntervals = normsPerIntervals;
        this.evaluateNormativeFlaws();
    }

    /**
     * Adds to the flaw list all flaws related to regulative norms and the situation in which they
     * are not applied, namely :
     * <ul>
     *     <li>Missing obligations</li>
     *     <li>Missing prohibitions</li>
     *     <li>Missing permissions</li>
     * </ul>
     */
    public void evaluateNormativeFlaws() {
        this.convertPermissionsToProhibitions();
        this.getSituations().forEach(situation -> this.getApplicableRegulativeNorms(situation)
                .forEach(applicableNorm -> {
                    if (!applicableNorm.isApplied(this, situation)) {
                        this.getFlaws().add(new NormativeFlaw(this, applicableNorm, situation));
                    }
                })
        );
    }

    private void convertPermissionsToProhibitions() {
        this.getActiveOrganizations().forEach(organization -> {

        });
    }

    /**
     * Evaluate all active organizations in the plan and updates its list in the plan.
     */
    public void evaluateActiveOrganizations() {
        this.activeOrganizations = new ArrayList<>();

        this.getOrganizations().forEach(organization -> {
            if (organization.hasRole(Keywords.AGENT_CONCEPT)) {
                this.activeOrganizations.add(organization);
            }
        });
    }

    /**
     * Solve a normative flaw depending on its deontic operator and consequence type (either a
     * proposition or an action that ought to be or not to be done).
     * @param normativeFlaw : the normative flaw to be solved
     * @param possibleActions : the set of all possible actions, necessary for solutions that
     *                        require adding in some new action, such as adding a mandatory
     *                        action.
     * @return a set of plan modifications that would solve the normative flaw.
     */
    public List<Operator> resolve(NormativeFlaw normativeFlaw, List<Action> possibleActions){
        return switch(normativeFlaw.getFlawedNorm().getDeonticOperator()){
            case OBLIGATION -> MissingObligationResolver.resolve(this, normativeFlaw,
                    possibleActions);
            case PROHIBITION -> MissingProhibitionResolver.resolve(this, normativeFlaw);
            default -> throw new UnsupportedOperationException("Cannot resolve deontic operator" +
                    "in the normative flaw");
        };
    }

    /**
     * Returns a list of all regulative norms applicable to a given situation of the current plan
     *
     * @param situation : the situation in the plan we want to check out for applicable norms
     * @return a list of regulative norms applicable to the given situation
     */
    private List<RegulativeNorm> getApplicableRegulativeNorms(PopSituation situation) {
        List<RegulativeNorm> applicableRegulativeNorms = new ArrayList<>();

        for (Organization organization : this.activeOrganizations) {
            List<RegulativeNorm> allOrganizationNorms = organization.getRegulativeNormsByConcept(
                    Keywords.AGENT_CONCEPT
            );

            List<RegulativeNorm> toAdd = allOrganizationNorms.stream()
                    .filter(norm -> hasAnyApplicabilityCodenotations(situation, norm))
                    .toList();


            applicableRegulativeNorms.addAll(toAdd);
        }

//        logger.info("All regulative norms applicable are : " + applicableRegulativeNorms);
        return applicableRegulativeNorms;
    }

    private boolean hasAnyApplicabilityCodenotations(PopSituation situation, RegulativeNorm norm) {
        try {
//            logger.info("=".repeat(100));
//            logger.info(norm);
//            logger.info("=".repeat(100));
            norm.getNormConditions().getApplicableCodenotations(this, situation);
            return true;
        } catch (UnapplicableNormException e) {
            return false;
        }
    }


    /**
     * Returns all the asserted proposition in a given situation of the plan by retrieving all
     * preceding steps and their consequences
     *
     * @param situation : the situation where we want to retrieve all asserted propositions from
     * @return a list of propositions i.e. of ContextualAtoms which are
     */
    public List<ContextualAtom> getAllAssertedPropositions(
            PopSituation situation
    ) {
        List<ContextualAtom> assertedPropositions = new ArrayList<>();

        this.getSteps().stream().filter(step -> this.getTc().isBefore(step, situation))
                .forEach(precedingStep -> {
                    assertedPropositions.addAll(getRemainingPropositions(precedingStep, situation));
                });

        return assertedPropositions;
    }

    @Override
    public State applyPlanModification(Operator toApply) {
        return ((PlanModification)toApply).apply(this);
    }

    private List<ContextualAtom> getRemainingPropositions(
            Step precedingStep,
            PopSituation situation
    ){
        List<ContextualAtom> remainingPropositions = new ArrayList<>();

        precedingStep.getActionConsequences().getAtoms().forEach(consequence -> {
            ContextualAtom toAdd = new ContextualAtom(
                    precedingStep.getActionInstance().getContext(), consequence
            );

            if (precedingStep.asserts(toAdd, this.getCc())) {
                List<Step> destroyers = this.getSteps().stream()
                        .filter(step -> step.destroys(toAdd, this.getCc()))
                        .filter(destroyer -> isBetween(precedingStep, destroyer, situation))
                        .toList();

                if (destroyers.isEmpty()) {
                    remainingPropositions.add(toAdd);
                }

                for (Step destroyer : destroyers) {
                    if (this.isRestablished(toAdd, destroyer, situation)) {
                        remainingPropositions.add(toAdd);
                    }
                }
            }
        });

        return remainingPropositions;
    }

    /**
     * Checks if some element in the plan is between two others. Namely, checks if the target
     * element is between the start and the finish element.
     * @param start : the element at the left edge of the interval we want to check
     * @param target: the element we want to check if it is between start and finish
     * @param finish: the element at the right edge of the interval we want to check
     * @return true if start < target < finish, and false otherwise.
     */
    private boolean isBetween(PlanElement start, PlanElement target, PlanElement finish) {
        return this.getTc().isBefore(start, target)
                && this.getTc().isBefore(target, finish);
    }

    @Override
    public List<Operator> resolve(Flaw toSolve, List<Action> possibleActions) {
        if(toSolve instanceof OpenCondition){
            return resolve((OpenCondition) toSolve, possibleActions);
        }else if(toSolve instanceof Threat){
            return resolve((Threat) toSolve);
        }else if(toSolve instanceof NormativeFlaw){
            return resolve((NormativeFlaw) toSolve, possibleActions);
        }

        throw new UnsupportedOperationException("Flaw type not supported yet.");

    }

    /**
     * Return all possible actions given by the all institutions, where the agent plays a role in
     * its organization.
     * @return the list of all possible actions based on the agent's roles in all active
     * institutions
     */
    public List<Action> getAllPossibleActionsFromInstitutions() {
        List<Action> allPossibleActionsFromInstitutions = new ArrayList<>();

        this.activeOrganizations.forEach(organization -> {
            List<Action> toAdd = organization.getInstitution().getPossibleActions();
            allPossibleActionsFromInstitutions.addAll(toAdd);
        });

        return allPossibleActionsFromInstitutions;
    }

    @Override
    public String toString() {
        return super.toString().concat(
                "\n--INSTITUTIONS\n" + this.activeOrganizations.toString()
        );
    }

}
