package aStar_planning.pop_with_norms.components;

import aStar.Operator;
import aStar.State;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.OpenCondition;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PlanElement;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import aStar_planning.pop.components.Threat;
import aStar_planning.pop.mapper.PlanModificationMapper;
import aStar_planning.pop_with_norms.resolvers.MissingObligationResolver;
import aStar_planning.pop_with_norms.resolvers.MissingProhibitionResolver;
import constraints.Codenotation;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import exception.UnapplicableNormException;
import logic.Action;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Context;
import logic.ContextualAtom;
import logic.ContextualTerm;
import logic.Predicate;
import logic.Term;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public class NormativePlan extends Plan {
    private final static Logger logger = LogManager.getLogger(NormativePlan.class);
    private final List<Organization> organizations;
    private final List<Role> roleKeywords;

    public NormativePlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations
    ) {
        super(situations, steps, cc, tc, true);
        this.organizations = organizations;
        this.roleKeywords = getAllRoles();
        addObligatoryPropositionsToGoals();
        super.evaluateFlaws(false);
        evaluateNormativeFlaws();
    }

    /**
     * Add the norm applicability conditions to the action preconditions as a result of some
     * conditional permission on the action
     *
     * @return List of all actions with their permissive conditions added if any.
     */
    private List<Action> buildPermittedNormativeActions() {
        List<Action> permittedActions = new ArrayList<>();
        List<RegulativeNorm> permissionOnActions = this.getPermissions()
                .stream()
                .filter(RegulativeNorm::enforceAction)
                .toList();

        for (RegulativeNorm permission : permissionOnActions) {
            Action targetedAction = (Action) permission.getNormConsequences();
            ActionPrecondition normativePreconditions = new ActionPrecondition(new ArrayList<>(
                    targetedAction.getPreconditions().getAtoms()
            ));

            for (Atom normativeCondition : permission.getNormConditions().getConditions()) {
                if (!isRole(normativeCondition)) {
                    normativePreconditions.getAtoms().add(normativeCondition);
                }
            }

            permittedActions.add(new Action(
                    targetedAction.getActionName(),
                    normativePreconditions,
                    targetedAction.getConsequences()
            ));
        }

        return permittedActions;
    }

    private void addObligatoryPropositionsToGoals() {
        List<RegulativeNorm> obligationOnPropositions = getAllRegulativeNorms()
                .stream()
                .filter(norm -> norm.isObligation() && norm.enforceProposition())
                .toList();

        for (PopSituation situation : this.getSituations()) {
            for (RegulativeNorm obligation : obligationOnPropositions) {
                if (this.isApplicable(situation, obligation)) {
                    addNormativeGoal(obligation);
                }
            }
        }
    }

    /**
     * Adds the current obligatory condition as goal to the planning problem.
     *
     * @param obligation : the obligation which is applicable and will be turned to a goal.
     */
    private void addNormativeGoal(RegulativeNorm obligation) {
        Atom consequence = (Atom) obligation.getNormConsequences();
        List<Atom> existingPreconditions = this.getFinalStep().getActionPreconditions().getAtoms();

        if (!existingPreconditions.contains(consequence)) {
            existingPreconditions.add(consequence);
        }
    }

    /**
     * Adds to the flaw list all flaws related to regulative norms and the situation in which they
     * are not applied, namely :
     * <ul>
     *     <li>Missing obligations</li>
     *     <li>Missing prohibitions</li>
     * </ul>
     */
    public void evaluateNormativeFlaws() {
        List<RegulativeNorm> allRegulativeNorms = new ArrayList<>(getAllRegulativeNorms());
        List<RegulativeNorm> obligationsOnActions = getObligationsOnActions(allRegulativeNorms);
        List<RegulativeNorm> prohibitions = getProhibitions(allRegulativeNorms);

        prohibitions.addAll(generatedProhibitionsFromPermissions(getAllRegulativeNorms()));

        evaluateObligationsPerSituation(obligationsOnActions);
        evaluateProhibitionsPerInterval(prohibitions);
    }

    /**
     * Checks if there is any normative flaw to be generated about applicable prohibitions.
     * This will check every applicable interval and generate normative flaw if it is not applied.
     *
     * @param prohibitions : all prohibitions to be tested and verified.
     */
    private void evaluateProhibitionsPerInterval(List<RegulativeNorm> prohibitions) {
        for (RegulativeNorm prohibition : prohibitions) {
            boolean isApplicable = false;
            for (PopSituation situation : this.getSituations()) {
                if(isApplicable(situation, prohibition)){
                    isApplicable = true;
                }
            }
            if(!isApplicable) continue;

            Interval applicableInterval = getApplicableInterval(prohibition);

            if (applicableInterval.isEmpty()) {
                continue;
            }

            Context startContext;
            try {
                startContext = getApplicableContext(prohibition, applicableInterval
                        .getBeginningSituation());
            } catch (UnapplicableNormException e) {
                continue;
            }

            this.addMissingProhibitionIfAny(applicableInterval, prohibition, startContext);
        }
    }

    /**
     * Checks if there is any normative flaw to be generated about applicable obligations.
     * Note that only obligations on actions needs to be treated as obligations on propositions
     * are already handled as goals.
     *
     * @param obligationsOnActions
     */
    private void evaluateObligationsPerSituation(List<RegulativeNorm> obligationsOnActions) {
        for (PopSituation situation : this.getSituations()) {
            for (RegulativeNorm norm : obligationsOnActions) {
                if (isApplicable(situation, norm)) {
                    Context applicableContext;

                    try {
                        applicableContext = getApplicableContext(norm, situation);
                    } catch (UnapplicableNormException e) {
                        continue;
                    }

                    this.addMissingObligationIfAny(situation, norm, applicableContext);
                }
            }
        }
    }

    /**
     * A helper function to filter out prohibitions inside a set of regulative norms
     *
     * @param allRegulativeNorms : the set of regulative norms to be filtered
     * @return all prohibitions from allRegulativeNorms
     */
    private List<RegulativeNorm> getProhibitions(List<RegulativeNorm> allRegulativeNorms) {
        return allRegulativeNorms
                .stream()
                .filter(RegulativeNorm::isProhibition)
                .collect(Collectors.toList());
    }

    /**
     * A helper function to filter out all obligations which enforces actions from a set of
     * regulative norms
     *
     * @param allRegulativeNorms : the set of regulative norms to be filtered
     * @return
     */
    private List<RegulativeNorm> getObligationsOnActions(List<RegulativeNorm> allRegulativeNorms) {
        return allRegulativeNorms
                .stream()
                .filter(norm -> norm.isObligation() && norm.enforceAction())
                .collect(Collectors.toList());
    }

    /**
     * A helper function to filter out all permissions from all regulative norms
     *
     * @return all permissions from all regulative norms
     */
    private List<RegulativeNorm> getPermissions() {
        return getAllRegulativeNorms()
                .stream()
                .filter(RegulativeNorm::isPermission)
                .collect(Collectors.toList());
    }

    public List<RegulativeNorm> generatedProhibitionsFromPermissions(
            List<RegulativeNorm> allRegulativeNorms
    ) {
        List<RegulativeNorm> generatedProhibitions = new ArrayList<>();

        List<RegulativeNorm> permissionsOnPropositions = allRegulativeNorms
                .stream()
                .filter(norm -> norm.isPermission() && norm.enforceProposition())
                .toList();

        for (RegulativeNorm permission : permissionsOnPropositions) {
            this.getSituations().stream()
                    .filter(situation -> !this.isApplicable(situation, permission))
                    .forEach(
                            insertProhibition(generatedProhibitions, permission)
                    );
        }

        return generatedProhibitions;
    }

    private Consumer<PopSituation> insertProhibition(
            List<RegulativeNorm> generatedProhibitions,
            RegulativeNorm permission
    ) {
        NormConditions reversedConditions = new NormConditions(permission.getNormConditions()
                .getConditions()
                .stream()
                .map(condition -> new Atom(!condition.isNegation(), condition.getPredicate()))
                .toList());

        RegulativeNorm toAdd = new RegulativeNorm(
                DeonticOperator.PROHIBITION,
                reversedConditions,
                permission.getNormConsequences()
        );
        return situation -> {
            if (!generatedProhibitions.contains(toAdd)) {
                generatedProhibitions.add(toAdd);
            }
        };
    }

    /**
     * Adds normative flaw if any is generated  by either :
     * a) an obligatory action: which will create a flaw that signals that a certain action needs to
     * be in the plan
     * b) a prohibited action: which will create a flaw that signals that a certain action should
     * not be within a certain interval of the plan
     * c) a prohibited proposition: which will create a flaw that signals that a certain atom
     * should not be within a certain interval of the plan
     * d) a permitted proposition: which will not generate normative flaws directly, but will
     * generate prohibitions instead.
     *
     * @param situation
     * @param norm
     * @param applicableContext
     */
    private void addMissingObligationIfAny(
            PopSituation situation,
            RegulativeNorm norm,
            Context applicableContext
    ) {
        if (norm.isObligation() && norm.enforceAction()) {
            boolean actionExists = this.getSteps()
                    .stream()
                    .anyMatch(step -> {
                        Action action = (Action) step.getActionInstance().getLogicalEntity();
                        NormativeAction normativeAction = (NormativeAction) norm
                                .getNormConsequences();
                        return action.getActionName().getName()
                                .equals(normativeAction.getActionName().getName());
                    });

            if (!actionExists) {
                this.addNormativeFlaw(situation, norm, applicableContext);
            }
        }
    }

    public void addMissingProhibitionIfAny(
            Interval applicableInterval,
            RegulativeNorm prohibition,
            Context intervalStartContext
    ) {
        if (prohibition.enforceAction()) {
            NormativeAction prohibitedAction = (NormativeAction) prohibition.getNormConsequences();

            if (actionExistsIn(prohibitedAction, applicableInterval)) {
                this.getFlaws().add(new NormativeFlaw(
                        this, prohibition, applicableInterval, intervalStartContext
                ));
            }
        } else {
            NormativeProposition prohibitedProposition = (NormativeProposition) prohibition.
                    getNormConsequences();

            boolean propositionExists = propositionExistsIn(
                    prohibition,
                    applicableInterval
            );

            if (propositionExists) {
                this.getFlaws().add(new NormativeFlaw(this, prohibition, applicableInterval,
                        intervalStartContext));
            }
        }
    }

    /**
     * Checks if some forbidden proposition is asserted or not inside some interval of situation
     *
     * @param prohibition:        the prohibition containing the proposition we want to check.
     * @param applicableInterval: the interval in which the norm is applicable in the plan.
     * @return true if the proposition exists in the given interval, and false otherwise.
     */
    private boolean propositionExistsIn(
            RegulativeNorm prohibition,
            Interval applicableInterval
    ) {
        NormativeProposition prohibitedProposition = (NormativeProposition) prohibition
                .getNormConsequences();

        return this.getSituations()
                .stream()
                .filter(situation -> isBetween(
                        applicableInterval.getBeginningSituation(), situation,
                        applicableInterval.getEndingSituation() == null ? getFinalSituation()
                                : applicableInterval.getEndingSituation())
                )
                .anyMatch(situation -> {
                    try {
                        Context conditionContext = new Context();
                        CodenotationConstraints applicableCodenotations = prohibition
                                .getApplicableCodenotations(this, situation, conditionContext);

                        ContextualAtom toCheck = new ContextualAtom(
                                conditionContext,
                                prohibitedProposition
                        );

                        return isAsserted(toCheck, situation, applicableCodenotations);
                    } catch (UnapplicableNormException e) {
                        return false;
                    }
                });
    }

    private boolean actionExistsIn(NormativeAction prohibitedAction, Interval applicableInterval) {
        return this.getSteps()
                .stream()
                .filter(step -> step.getAction().sameName(prohibitedAction))
                .anyMatch(step -> isBetween(
                        applicableInterval.getBeginningSituation(),
                        step,
                        applicableInterval.getEndingSituation() == null ?
                                getFinalSituation()
                                : applicableInterval.getEndingSituation())
                );
    }

    /**
     * Return the interval where a norm is applicable in the plan, from its earliest to its latest
     * applicable situation
     *
     * @param norm
     * @return
     */
    private Interval getApplicableInterval(RegulativeNorm norm) {
        if (norm.enforceAction()) {
            PopSituation begin = getFirstApplicableSituation(norm);
            PopSituation end = getInapplicableSituationAfter(begin, norm);

            return new Interval(begin, end);
        } else {
            // TODO: urgent
            NormativeProposition prohibited = (NormativeProposition) norm.getNormConsequences();
            Step establisher = getEstablisher(prohibited, this.getFinalSituation());

            if(establisher == null){
                return new Interval(null, null);
            }

            PopSituation begin = this.getTc().getFollowingSituation(establisher);
            PopSituation end = getInapplicableSituationAfter(begin, norm);

            return new Interval(begin, end);
        }
    }

    private Step getAssertingStep(RegulativeNorm prohibition, PopSituation finalSituation) {
        for (Step step : this.getSteps().stream().filter(this::isNotFinalStep).toList()) {
            Context conditionContext = new Context();
            PopSituation followingSituation = this.getTc().getFollowingSituation(step);
            CodenotationConstraints applicableCc = prohibition.getApplicableCodenotations(
                    this, followingSituation,conditionContext
            );
            NormativeProposition prohibited = (NormativeProposition) prohibition
                    .getNormConsequences();

            if(isAsserted(
                    new ContextualAtom(conditionContext, prohibited),
                    followingSituation,
                    applicableCc)
            ){
                return step;
            }
        }
        return null;
    }

    /**
     * Fetches the first situation in the plan where the norm is applicable.
     *
     * @param norm: the norm to be checked if applicable
     * @return the earliest situation where it is applicable.
     */
    public PopSituation getFirstApplicableSituation(RegulativeNorm norm) {
        PopSituation initialSituation = this.getInitialSituation();
        if (hasApplicableContext(norm, initialSituation)) {
            return initialSituation;
        }

        PopSituation firstApplicableSituation = null;
        for (PopSituation situation : this.getSituations()) {
            try {
                Context conditionContext = getApplicableContext(norm, situation);
                norm.getApplicableCodenotations(this, situation, conditionContext);

                if (firstApplicableSituation == null ||
                        this.getTc().isBefore(situation, firstApplicableSituation)
                ) {
                    firstApplicableSituation = situation;
                }
            } catch (UnapplicableNormException e) {
                continue;
            }
        }

        return firstApplicableSituation;
    }

    private boolean hasApplicableContext(RegulativeNorm norm, PopSituation situation) {
        try {
            this.getApplicableContext(norm, situation);
        } catch (UnapplicableNormException e) {
            return false;
        }
        return true;
    }

    /**
     * Get the non-applicable situation in which the norm stops being applicable
     *
     * @param applicableSituation : the starting situation where the interval will start.
     * @return
     */
    public PopSituation getInapplicableSituationAfter(
            PopSituation applicableSituation,
            RegulativeNorm flawedNorm
    ) {
        if (applicableSituation == null) {
            return null;
        }

        PopSituation inapplicableSituation = null;
        List<PopSituation> situationsAfterApplication = this.getSituations()
                .stream()
                .filter(situation -> this.getTc().isBefore(applicableSituation, situation))
                .toList();

        for (PopSituation situation : situationsAfterApplication) {
            try {
                Context conditionContext = getApplicableContext(flawedNorm, situation);
                flawedNorm.getApplicableCodenotations(this, situation, conditionContext);
            } catch (UnapplicableNormException e) {
                if (inapplicableSituation == null ||
                        this.getTc().isBefore(situation, inapplicableSituation)
                ) {
                    inapplicableSituation = situation;
                }
            }
        }

        return inapplicableSituation;
    }

    /**
     * Adds a normative flaw to the set of flaws inside the plan. This happens when a regulative
     * norm is applicable but not applied as it should.
     *
     * @param situation is where the norm is applicable but not applied
     * @param norm      : the regulative norm concerned by the normative flaw
     */
    public void addNormativeFlaw(
            PopSituation situation,
            RegulativeNorm norm,
            Context applicableContext
    ) {
        this.getFlaws().add(new NormativeFlaw(
                this,
                norm,
                situation,
                applicableContext
        ));
    }

    /**
     * Adds all the actions from the set of institutions organized in this planning problem.
     * This searches through all active organizations and fetches actions attached to the role the
     * agent plays.
     *
     * @return a list of all possible actions based upon all active organizations.
     */
    public List<Action> getActionsFromAllInstitutions() {
        List<Action> possibleActions = new ArrayList<>();

        for (Organization organization : this.organizations) {
            possibleActions.addAll(organization.allActionsForAgent());
        }

        return possibleActions;
    }

    /**
     * Verifies if a norm is applicable by verifying all of its conditions, which either demands
     * some constitutive norm, or some proposition to be necessarily true
     *
     * @param situation : the situation on which we want to check the norm's applicability
     *                  conditions
     * @param norm      : the norm whose applicability conditions will be tested.
     * @return true if the norm is applicable to the given situation, false otherwise.
     */
    public boolean isApplicable(PopSituation situation, RegulativeNorm norm) {
        Context conditionContext = new Context();

        for (Atom condition : norm.getNormConditions().getConditions()) {
            if (isRole(condition)) {
                if (!validatedByConstitutiveNorms(
                        condition,
                        conditionContext,
                        this.getCc().copy()
                )) {
                    return false;
                }
            } else {
                if (!isSatisfiedInSituation(situation, conditionContext, condition)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a condition is satisfied in a given situation of the plan.
     *
     * @param situation        : the situation in which we want to check if the condition is necessarily
     *                         true.
     * @param conditionContext : the context of the condition
     * @param condition        : the condition we want to check if it is necessarily true, mainly some
     *                         applicability condition
     * @return true if the condition is necessarily true in the given situation, and false otherwise
     */
    private boolean isSatisfiedInSituation(
            PopSituation situation,
            Context conditionContext,
            Atom condition
    ) {
        CodenotationConstraints cc = new CodenotationConstraints();
        Predicate conditionPredicate = condition.getPredicate();
        boolean isUnifiedOnce = false;

        for (ContextualAtom assertedProposition : getAllAssertedPropositions(situation)) {
            if (conditionPredicate.unify(
                    conditionContext,
                    assertedProposition.getAtom().getPredicate(),
                    assertedProposition.getContext(),
                    cc
            )) {
                isUnifiedOnce = true;
                break;
            }
        }

        return condition.isNegation() ? !isUnifiedOnce : isUnifiedOnce;
    }

    /**
     * Determines if an atom is a constitutive norm verification. Meaning that the atom requires
     * some constitutive norms, or else it is false.
     *
     * @param condition : the condition to check if it designates a role
     * @return true if the atom is referring to a constitutive norm, and false otherwise.
     */
    public boolean isRole(Atom condition) {
        return this.roleKeywords
                .stream()
                .anyMatch(role -> Objects.equals(role.getName(), condition.getPredicate().getName()));
    }

    /**
     * Checks if a condition matches with some constitutive norms of either the organization
     * or the institution
     *
     * @param condition : the proposition we want to verify as a constitutive norm
     * @return true if the condition is confirmed by some constitutive norm, and false otherwise
     */
    public boolean validatedByConstitutiveNorms(
            Atom condition,
            Context conditionContext,
            CodenotationConstraints ccCopy
    ) {
        Context normContext = new Context();

        Term subject = condition.getPredicate().getTerms().get(0);
        String roleName = condition.getPredicate().getName();

        for (ConstitutiveNorm constitutiveNorm : this.getAllConstitutiveNorms()) {
            if ((roleName.equals(constitutiveNorm.getTarget().getName()))
                    && (constitutiveNorm.getSource().unify(
                    normContext,
                    subject,
                    conditionContext,
                    ccCopy))
            ) {
//                if(condition.getPredicate().getName().equals("member")){
//                    logger.warn("Condition : " + condition + " is valid with cc : " + ccCopy);
//                }
                return !condition.isNegation() && ccCopy.isTransitivelyCoherent();
            }
        }
        return false;
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

    public List<Action> getPermittedActions(List<Action> possibleActions) {
        List<Action> permittedActions = new ArrayList<>(possibleActions);

        this.getAllRegulativeNorms()
                .stream()
                .filter(norm -> norm.isPermission() && norm.enforceAction())
                .forEach(permission -> permittedActions.removeIf(
                        action -> action.equals(permission.getNormConsequences())
                ));

        permittedActions.addAll(buildPermittedNormativeActions());

        return permittedActions;
    }

    /**
     * Retrieves the context of application of a norm.
     * TODO : write the docs here for both obligations and prohibitions.
     *
     * @param norm
     * @param situation
     * @return
     */
    private Context getApplicableContext(RegulativeNorm norm, PopSituation situation) {
        if (norm.enforceProposition()) {
            Step followingStep = this.getTc().getFollowingStep(situation);
            return followingStep.getActionInstance().getContext();
        } else if (norm.enforceAction()) {
            if (norm.getDeonticOperator().equals(DeonticOperator.OBLIGATION)) {
                return new Context();
            } else if (norm.getDeonticOperator().equals(DeonticOperator.PROHIBITION)) {
                Action forbiddenAction = ((NormativeAction) norm.getNormConsequences());

                Optional<Step> forbiddenStep = this.getSteps().stream()
                        .filter(step -> this.getTc().isBefore(situation, step))
                        .filter(step -> step.getActionInstance().getLogicalEntity()
                                .equals(forbiddenAction))
                        .findFirst();

                if (forbiddenStep.isPresent()) {
                    return forbiddenStep.get().getActionInstance().getContext();
                }
            }
        }
        throw new UnapplicableNormException(norm.getNormConditions(), situation);
    }

    /**
     * Returns all applicable regulative norms imposed by all current organizations the agent is a
     * part of. Organizations it is not a part of are simply not listed.
     *
     * @return all applicable regulative norms from all organizations
     */
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
     * Returns all the asserted proposition in a given situation of the plan by retrieving all
     * preceding steps and their consequences
     *
     * @param situation : the situation where we want to retrieve all asserted propositions from
     * @return a list of propositions i.e. of ContextualAtoms which are
     */
    public List<ContextualAtom> getAllAssertedPropositions(PopSituation situation) {
        List<ContextualAtom> assertedPropositions = new ArrayList<>();

        this.getSteps().stream().filter(step -> !this.getTc().isAfter(step, situation))
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
    public State applyPlanModification(Operator toApply) {
        return ((PlanModification) toApply).apply(this);
    }

    @Override
    public List<Operator> resolve(Flaw toSolve, List<Action> possibleActions) {
        List<Action> permittedActions = getPermittedActions(possibleActions);

        if (toSolve instanceof OpenCondition) {
            return resolve((OpenCondition) toSolve, permittedActions);
        } else if (toSolve instanceof Threat) {
            return resolve((Threat) toSolve);
        } else if (toSolve instanceof NormativeFlaw) {
            return resolve((NormativeFlaw) toSolve, permittedActions);
        }

        throw new UnsupportedOperationException("Flaw type not supported yet.");
    }

    /**
     * Detects all the open conditions within the plan, all the preconditions of actions which
     * are not necessarily true in their preceding situation.
     *
     * @param step : the step to analyze
     * @return
     */
    @Override
    protected List<Flaw> getOpenConditions(Step step) {
        List<Flaw> openConditions = new ArrayList<>();
        List<Atom> stepPreconditions = step.getActionPreconditions().getAtoms();
        CodenotationConstraints temporaryCc = this.getCc().copy();

        for (Atom precondition : stepPreconditions) {
            if (isRole(precondition)) {
                if (!validatedByConstitutiveNorms(
                        precondition,
                        step.getActionInstance().getContext(),
                        temporaryCc
                )) {
                    openConditions.add(buildOpenCondition(precondition, step));
                }
            } else {
                ContextualAtom preconditionInstance = new ContextualAtom(
                        step.getActionInstance().getContext(), precondition
                );

                if (!isAsserted(preconditionInstance, this.getTc().getPrecedingSituation(step),
                        temporaryCc)) {
                    openConditions.add(buildOpenCondition(precondition, step));
                }
            }
        }

        return openConditions;
    }

    @Override
    public boolean isAsserted(
            ContextualAtom proposition,
            PopSituation situation,
            CodenotationConstraints cc
    ) {
        return this.getSteps().stream()
                .filter(this::isNotFinalStep)
                .anyMatch(step -> this.getTc().isBefore(step, situation)
                        && step.assertsWithPermanentCodenotations(proposition, cc)
                );
    }

    @Override
    public List<Operator> resolve(OpenCondition openCondition, List<Action> possibleActions) {
        if (this.isRole(openCondition.getProposition().getAtom())) {
            return resolveRole(openCondition);
        }
        return super.resolve(openCondition, possibleActions);
    }

    /**
     * Specify how you want to resolve a missing constitutive norm
     *
     * @param openCondition : the open condition you want to resolve
     * @return a list of operators based upon the set of constitutive norm
     */
    private List<Operator> resolveRole(OpenCondition openCondition) {
        List<CodenotationConstraints> additions = new ArrayList<>();
        ContextualAtom missingCondition = openCondition.getProposition();
        Context initialContext = this.getInitialStep().getActionInstance().getContext();
        Term onlyTermToCodenotate = missingCondition.getAtom().getPredicate().getTerms().get(0);

        this.getAllConstitutiveNorms()
                .stream()
                .filter(constitutiveNorm -> constitutiveNorm.getTarget().getName()
                        .equals(missingCondition.getAtom().getPredicate().getName()))
                .forEach(constitutiveNorm -> {
                    if (onlyTermToCodenotate.unify(
                            missingCondition.getContext(),
                            constitutiveNorm.getSource(),
                            initialContext,
                            this.getCc().copy())
                    ) {
                        additions.add(new CodenotationConstraints(List.of(
                                new Codenotation(!missingCondition.getAtom().isNegation(),
                                        new ContextualTerm(
                                                openCondition.getProposition().getContext(),
                                                onlyTermToCodenotate),
                                        new ContextualTerm(
                                                initialContext,
                                                constitutiveNorm.getSource()
                                        )
                                )
                        )));
                    }
                });

        return additions.stream()
                .map(foundCc -> (Operator) PlanModificationMapper.from(openCondition, foundCc))
                .toList();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n-- ORGANIZATIONS : \n\t" +
                this.organizations;
    }

    /**
     * Fetches the step which established a proposition in the plan in a given situation
     *
     * @param proposition : the proposition to be established
     * @param situation   : the situation in which it is made necessarily true
     * @return : the step which establishes (or asserts) the `proposition` in `situation`
     */
    public Step getEstablisher(NormativeProposition proposition, PopSituation situation) {
        List<Step> precedingOrParallelSteps = this.getSteps()
                .stream()
                .filter(step -> !this.getTc().isAfter(step, situation))
                .toList();

        for (Step step : precedingOrParallelSteps) {
            Context contextPerStep = new Context();
            ContextualAtom propositionWithContext = new ContextualAtom(contextPerStep, proposition);

            if (step.asserts(propositionWithContext, this.getCc())) {
                return step;
            }
        }

        return null;
    }
}
