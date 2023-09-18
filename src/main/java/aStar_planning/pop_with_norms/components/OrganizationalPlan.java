package aStar_planning.pop_with_norms.components;

import aStar.Operator;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.Threat;
import aStar_planning.pop_with_norms.resolvers.MissingObligationResolver;
import aStar_planning.pop_with_norms.resolvers.MissingProhibitionResolver;
import aStar_planning.pop_with_norms.utils.PermissionMapper;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import exception.UnapplicableNormException;
import logic.Action;
import logic.Context;
import logic.ContextualAtom;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
public class OrganizationalPlan extends Plan {
    private final static Logger logger = LogManager.getLogger(OrganizationalPlan.class);
    private final List<Organization> organizations;

    public OrganizationalPlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations
    ) {
        super(situations, steps, cc, tc);
        this.organizations = organizations;
        evaluateNormativeFlaws();
    }

    /**
     * Adds to the flaw list all flaws related to regulative norms and the situation in which they
     * are not applied, namely :
     * <ul>
     *     <li>Missing obligations</li>
     *     <li>Missing prohibitions</li>
     *     <li>Missing permissions : which needs to be converted to prohibitions first</li>
     * </ul>
     */
    public void evaluateNormativeFlaws() {
        List<RegulativeNorm> generatedProhibitions = this.convertPermissionsToProhibitions();

        for (PopSituation situation : this.getSituations()) {
            this.getAllRegulativeNorms()
                    .forEach(applicableNorm -> {
                        if (isApplicable(situation, applicableNorm)) {
                            addFlawIfNotInserted(situation, applicableNorm);
                        }
                    });

            generatedProhibitions.forEach(prohibition -> {
                //logger.info("is applicable : " + prohibition + " in " + situation);
                if (isApplicable(situation, prohibition)) {
                    addFlawIfNotInserted(situation, prohibition);
                }
            });
        }
    }

    private boolean isApplicable(PopSituation situation, RegulativeNorm norm) {
        try {
            norm.getNormConditions().getApplicableCodenotations(this, situation);
            return true;
        } catch (UnapplicableNormException e) {
            return false;
        }
    }

    private void addFlawIfNotInserted(PopSituation situation, RegulativeNorm applicableNorm) {
        Context applicableContext = new Context();

        if (!applicableNorm.isApplied(this, situation)) {
            logger.debug(applicableNorm + " is not applied in " + situation);

            if (applicableNorm.getDeonticOperator().equals(DeonticOperator.PROHIBITION)) {
                applicableContext = getApplicableContext(applicableNorm, situation);
            }

            this.getFlaws().add(new NormativeFlaw(
                    this,
                    applicableNorm,
                    situation,
                    applicableContext
            ));
        }
    }

    @Override
    public List<Operator> resolve(Flaw toSolve, List<Action> possibleActions) {
        if (toSolve instanceof OpenCondition) {
            return resolve((OpenCondition) toSolve, possibleActions);
        } else if (toSolve instanceof Threat) {
            return resolve((Threat) toSolve);
        } else if (toSolve instanceof NormativeFlaw) {
            return resolve((NormativeFlaw) toSolve, possibleActions);
        }

        throw new UnsupportedOperationException("Flaw type not supported yet.");
    }

    /**
     * Solve a normative flaw depending on its deontic operator and consequence type (either a
     * proposition or an action that ought to be or not to be done).
     * @param normativeFlaw   : the normative flaw to be solved
     * @param possibleActions : the set of all possible actions, necessary for solutions that
     *                        require adding in some new action, such as adding a mandatory
     *                        action.
     * @return a set of plan modifications that would solve the normative flaw.
     */
    public List<Operator> resolve(NormativeFlaw normativeFlaw, List<Action> possibleActions) {
        return switch (normativeFlaw.getFlawedNorm().getDeonticOperator()) {
            case OBLIGATION -> MissingObligationResolver.resolve(
                    this,
                    normativeFlaw,
                    possibleActions);
            case PROHIBITION -> MissingProhibitionResolver.resolve(this, normativeFlaw,
                    possibleActions);
            default -> throw new UnsupportedOperationException("Cannot resolve deontic operator" +
                    "in the normative flaw");
        };
    }


    /**
     * Converts all permissions to prohibitions, and returns all resulting prohibitions
     */
    private List<RegulativeNorm> convertPermissionsToProhibitions() {
        List<RegulativeNorm> prohibitions = new ArrayList<>();

        this.getOrganizations().forEach(organization -> {
            organization.getNorms()
                    .stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .filter(this::isPermission)
                    .forEach(permission -> prohibitions.addAll(
                            PermissionMapper.toProhibitions(this, permission)
                    ));
        });

        return prohibitions;
    }

    private boolean isPermission(RegulativeNorm regulativeNorm) {
        return regulativeNorm.getDeonticOperator().equals(DeonticOperator.PERMISSION);
    }

    private Context getApplicableContext(RegulativeNorm norm, PopSituation situation) {
        if (norm.enforceProposition()) {
//          TODO: norm.getNormConditions().getApplicableCodenotations(this, situation);
        } else if (norm.enforceAction()) {
            Action forbiddenAction = ((NormativeAction) norm.getNormConsequences());
            logger.error(this.getSteps().stream()
                    .filter(step -> this.getTc().isBefore(situation, step))
                    .collect(Collectors.toList()));
            Optional<Step> forbiddenStep = this.getSteps().stream()
                    .filter(step -> this.getTc().isBefore(situation, step))
                    .filter(step -> step.getActionInstance().getLogicalEntity()
                            .equals(forbiddenAction))
                    .findFirst();

            logger.error("f step " + forbiddenStep);
            if (forbiddenStep.isPresent()) {
                logger.debug("Found context of forbidden action in norm " + norm + " with " + forbiddenStep.get());
                return forbiddenStep.get().getActionInstance().getContext();
            }
        }
        throw new RuntimeException("Applicable context could not be found for : " + norm);
    }

    private List<RegulativeNorm> getAllRegulativeNorms() {
        List<RegulativeNorm> regulativeNorms = new ArrayList<>();

        for (Organization organization : this.organizations) {
            regulativeNorms.addAll(organization.getNorms().stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .toList());
            regulativeNorms.addAll(organization.getInstitution().getNorms().stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .toList());
        }

        return regulativeNorms;
    }

    /**
     * Returns a list of all regulative norms applicable to a given situation of the current pla
     * @param situation : the situation in the plan we want to check out for applicable norms
     * @return a list of regulative norms applicable to the given situation
     */
    private List<RegulativeNorm> getApplicableRegulativeNorms(PopSituation situation) {
        List<RegulativeNorm> applicableRegulativeNorms = new ArrayList<>();

        for (Organization organization : this.organizations) {
            List<RegulativeNorm> toAdd = organization.getNorms()
                    .stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .filter(norm -> isApplicable(situation, norm))
                    .toList();

            applicableRegulativeNorms.addAll(toAdd);
        }

        return applicableRegulativeNorms;
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

    private List<ContextualAtom> getRemainingPropositions(
            Step precedingStep,
            PopSituation situation
    ) {
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
     *
     * @param start   : the element at the left edge of the interval we want to check
     * @param target: the element we want to check if it is between start and finish
     * @param finish: the element at the right edge of the interval we want to check
     * @return true if start < target < finish, and false otherwise.
     */
    private boolean isBetween(PlanElement start, PlanElement target, PlanElement finish) {
        return this.getTc().isBefore(start, target) && this.getTc().isBefore(target, finish);
    }
}