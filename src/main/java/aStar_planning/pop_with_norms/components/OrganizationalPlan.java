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
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.Term;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class OrganizationalPlan extends Plan {
    private final static Logger logger = LogManager.getLogger(OrganizationalPlan.class);
    private final List<Organization> organizations;
    private final List<Role> roleKeywords;

    public OrganizationalPlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations
    ) {
        super(situations, steps, cc, tc);
        this.organizations = organizations;
        this.roleKeywords = getAllRoles();
        evaluateNormativeFlaws();
    }

    /**
     * Adds to the flaw list all flaws related to regulative norms and the situation in which they
     * are not applied, namely :
     * <ul>
     *     <li>Missing obligations</li>
     *     <li>Missing prohibitions</li>
     *     <li>Missing permissions (which needs to be converted to prohibitions)</li>
     * </ul>
     */
    public void evaluateNormativeFlaws() {
        List<RegulativeNorm> toEvaluate = new ArrayList<>();
        toEvaluate.addAll(this.convertPermissionsToProhibitions());
        toEvaluate.addAll(getAllObligationsAndProhibitions());

        for (PopSituation situation : this.getSituations()) {
            for (RegulativeNorm norm : toEvaluate) {
                if (isApplicable(situation, norm)) {
                    createFlawIfNormNotApplied(situation, norm);
                }
            }
        }
    }

    /**
     * TODO : make sure to detect all constitutive norms through the special predicates of
     * the regulative norm.
     *
     * @param situation : the situation on which we want to check the norm's applicability
     *                  conditions
     * @param norm      : the norm whose applicability conditions will be tested.
     * @return true if the norm is applicable to the given situation, false otherwise.
     */
    public boolean isApplicable(PopSituation situation, RegulativeNorm norm) {
        Context conditionContext = new Context();
        Context normContext = new Context();
        for (Atom condition : norm.getNormConditions().getConditions()) {
            boolean isRole = this.roleKeywords
                    .stream()
                    .anyMatch(role -> role.getName().equals(condition.getPredicate().getName()));

            if (isRole) {
                if (!existsInConstitutiveNorms(condition, conditionContext, normContext)) {
                    return false;
                }
            } else {
                if (!hasApplicableCodenotations(situation, norm)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a condition matches with some constitutive norms of either the organization
     * or the institution
     *
     * @param condition : the proposition we want to verify as a constitutive norm
     * @return true if the condition is confirmed by some constitutive norm, and false otherwise
     */
    private boolean existsInConstitutiveNorms(
            Atom condition,
            Context conditionContext,
            Context normContext
    ) {
        Term subject = condition.getPredicate().getTerms().get(0);
        String roleName = condition.getPredicate().getName();

        for (ConstitutiveNorm constitutiveNorm : this.getAllConstitutiveNorms()) {
            if ((roleName.equals(constitutiveNorm.getTarget().getName()))
                    && (subject.unify(
                    conditionContext,
                    constitutiveNorm.getSource(),
                    normContext,
                    this.getCc()))
            ){
                return true;
            }
        }

        return false;
    }

    private boolean hasApplicableCodenotations(PopSituation situation, RegulativeNorm norm) {
        try {
            norm.getNormConditions().getApplicableCodenotations(this, situation);
            return true;
        } catch (UnapplicableNormException e) {
            return false;
        }
    }

    /**
     * Creates and inserts a new flaw in this plan's flaw set if the norm is not applied correctly.
     * @param situation : the situation in which the norm ought to be applied
     * @param applicableNorm : the norm which ought to be applied.
     */
    private void createFlawIfNormNotApplied(PopSituation situation, RegulativeNorm applicableNorm) {
        Context applicableContext = new Context();

        if (!applicableNorm.isApplied(this, situation)) {
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
     *
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
     * TODO : fix this by checking first which situations is it applicable in.
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

    /**
     * TODO: think if we really need a context, and where is it coming from ?
     *
     * @param norm
     * @param situation
     * @return
     */
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

    private List<RegulativeNorm> getAllObligationsAndProhibitions() {
        List<RegulativeNorm> regulativeNorms = new ArrayList<>();
        Predicate<RegulativeNorm> isNotPermission = (regulativeNorm -> !regulativeNorm
                .getDeonticOperator().equals(DeonticOperator.PERMISSION));

        for (Organization organization : this.organizations) {
            regulativeNorms.addAll(organization.getNorms().stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .filter(isNotPermission)
                    .toList());
            regulativeNorms.addAll(organization.getInstitution().getNorms().stream()
                    .filter(norm -> norm instanceof RegulativeNorm)
                    .map(norm -> (RegulativeNorm) norm)
                    .filter(isNotPermission)
                    .toList());
        }

        return regulativeNorms;
    }

    private List<ConstitutiveNorm> getAllConstitutiveNorms() {
        List<ConstitutiveNorm> constitutiveNorms = new ArrayList<>();

        for (Organization organization : this.organizations) {
            constitutiveNorms.addAll(organization.getNorms().stream()
                    .filter(norm -> norm instanceof ConstitutiveNorm)
                    .map(norm -> (ConstitutiveNorm) norm)
                    .toList());
            constitutiveNorms.addAll(organization.getInstitution().getNorms().stream()
                    .filter(norm -> norm instanceof ConstitutiveNorm)
                    .map(norm -> (ConstitutiveNorm) norm)
                    .toList());
        }

        return constitutiveNorms;
    }

    /**
     * Returns a list of all regulative norms applicable to a given situation of the current pla
     *
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

    private List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();

        for (Organization organization : this.organizations) {
            List<Role> rolesFromInstitution = organization.getInstitution().getConcepts()
                    .stream()
                    .filter(concept -> concept.getClass() == Role.class)
                    .map(constant -> (Role) constant)
                    .toList();

            roles.addAll(rolesFromInstitution);
        }

        return roles;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n-- ORGANIZATIONS : \n\t" +
                this.organizations;
    }
}