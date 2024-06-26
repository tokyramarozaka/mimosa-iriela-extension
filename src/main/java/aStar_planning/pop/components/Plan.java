package aStar_planning.pop.components;

import aStar.Operator;
import aStar.State;
import aStar_planning.pop.resolvers.OpenConditionResolver;
import aStar_planning.pop.resolvers.ThreatResolver;
import aStar_planning.pop.utils.PlanInitializer;
import constraints.TemporalConstraints;
import outputs.graphical_output.GraphvizGenerator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import logic.Action;
import logic.Atom;
import constraints.CodenotationConstraints;
import logic.ContextualAtom;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.Keywords;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A plan is a sequence of situations, steps, codenotation constraints, and
 * temporal constraints.
 * <ul>
 * <li>Situations : discrete points of the plan arranged by a set of temporal
 * constraints</li>
 * <li>Steps : action instances which can be arranged through a set of temporal
 * constraints</li>
 * <li>Codenotation constraints : variable bindings of the plan</li>
 * <li>Temporal constraints : arrangement of all steps and situations inside of
 * the plan</li>
 * </ul>
 */
@Getter
@EqualsAndHashCode
public class Plan implements State {
    private final static Logger logger = LogManager.getLogger(Plan.class);
    private static final AtomicInteger sequence = new AtomicInteger(0);
    private final int id;
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints cc;
    private TemporalConstraints tc;
    private Set<Flaw> flaws;

    public Plan() {
        this.id = sequence.incrementAndGet();
    }

    @Builder
    public Plan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc) {
        this.id = sequence.incrementAndGet();
        this.situations = situations;
        this.steps = steps;
        this.cc = cc;
        this.tc = tc;
        this.tc.updateGraph();
        this.evaluateFlaws(true);
    }

    /**
     * Builds the plan without computing the set of flaws right away, this was
     * designed to
     * compute flaws at a later time.
     *
     * @param situations
     * @param steps
     * @param cc
     * @param tc
     * @param evaluateFlaws just a simple boolean to overload the default
     *                      constructor which does
     *                      evaluate flaws upon creating the plan
     */
    public Plan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            boolean evaluateFlaws) {
        this.id = sequence.incrementAndGet();
        this.situations = situations;
        this.steps = steps;
        this.cc = cc;
        this.tc = tc;
        this.tc.updateGraph();
        this.flaws = new HashSet<>();
    }

    public boolean isNotFinalStep(Step step) {
        Action action = (Action) step.getActionInstance().getLogicalEntity();

        return !action.getActionName().getName().equals(Keywords.POP_FINAL_STEP.getName());
    }

    /**
     * (Re)computes all the flaws of the plan's steps : its open conditions and its
     * threats
     * Note that the initial step cannot have any threats since it is the first
     * step.
     */
    public void evaluateFlaws(boolean resetFlaws) {
        if (resetFlaws) {
            this.flaws = new HashSet<>();
        }

        for (Step step : this.steps) {
            if (!isInitialStep(step)) {
                this.flaws.addAll(this.getThreats(step));
            }

            this.flaws.addAll(this.getOpenConditions(step));
        }
    }

    /**
     * Computes the set of all possible modifications all plan flaws
     *
     * @return a List of all possible Plan modifications to solve all flaws in the
     * flaw list
     */
    public List<Operator> allPossibleModifications(List<Action> possibleActions) {
        List<Operator> possibleModifications = new ArrayList<>();

        flaws.forEach(flaw -> possibleModifications.addAll(resolve(flaw, possibleActions)));

        return possibleModifications;
    }

    /**
     * Resolves a flaw depending on its type. A list of possible actions is required
     * to resolve
     * open conditions as some action instances might be created.
     *
     * @param toSolve : the flaw which we want to solve
     * @return the list of plan modifications we can make to solve the given flaw.
     */
    public List<Operator> resolve(Flaw toSolve, List<Action> possibleActions) {
        if (toSolve instanceof OpenCondition) {
            return resolve((OpenCondition) toSolve, possibleActions);
        } else if (toSolve instanceof Threat) {
            return resolve((Threat) toSolve);
        }
        throw new UnsupportedOperationException("This type of flaw is not implemented yet");
    }

    /**
     * Resolves an open condition using different strategies.
     *
     * @param openCondition: the step precondition to be satisfied in its preceding
     *                       step
     * @return the list of plan modifications which would solve the given open
     * condition
     */
    public List<Operator> resolve(OpenCondition openCondition, List<Action> possibleActions) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(OpenConditionResolver.byPromotion(this, openCondition));
        resolvers.addAll(OpenConditionResolver.byCodenotation(this, openCondition));
        resolvers.addAll(OpenConditionResolver.byCreation(this, openCondition, possibleActions));

        return resolvers;
    }

    /**
     * Return all the plan modifications which resolves the given threat
     *
     * @param threat : the threat to resolve describing which step is threatening
     *               which step's
     *               precondition
     * @return a set of plan modifications that remove the threat once applied.
     */
    public List<Operator> resolve(Threat threat) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(ThreatResolver.byDestroyerDemotion(this, threat));
        resolvers.addAll(ThreatResolver.byNonCodenotation(this, threat));
        resolvers.addAll(ThreatResolver.byRestablishingStep(this, threat));

        return resolvers;
    }

    /**
     * Determines if a plan is executable : all step preconditions are necessarily
     * true in
     * their preceding situation respectively (there is no open condition and no
     * threat)
     *
     * @return true if the plan is executable, i.e. has no more flaw, false
     * otherwise
     */
    public boolean isExecutable() {
        return this.flaws.size() == 0;
    }

    /**
     * Checks if the current plan is coherent. A plan is coherent, iff :
     * <ul>
     * <li>The set of codenotation constraints are coherent (not contradictory)</li>
     * <li>The set of temporal constraints are coherent (not contradictory and not
     * cyclic)</li>
     * </ul>
     *
     * @return true if the plan is coherent, false otherwise.
     */
    public boolean isCoherent() {
        return this.cc.isCoherent() && this.tc.isCoherent();
    }

    /**
     * Apply a set of modifications on a plan, and returns the resulting plan
     *
     * @param toApply : the plan modification to be applied
     * @return the plan resulting from the applied modifications.
     */
    public State applyPlanModification(Operator toApply) {
        return ((PlanModification) toApply).apply(this);
    }

    private boolean isInitialStep(Step step) {
        Action stepAction = ((Action) step.getActionInstance().getLogicalEntity());

        return getActionName(stepAction).equals(Keywords.POP_INITIAL_STEP.getName());
    }

    private String getActionName(Action stepAction) {
        return stepAction.getActionName().getName();
    }

    /**
     * Get all open conditions for a given step.
     *
     * @param step : the step to analyze
     * @return all open conditions regarding a specific step
     */
    protected List<Flaw> getOpenConditions(Step step) {
        List<Flaw> openConditions = new ArrayList<>();
        List<Atom> stepPreconditions = step.getActionPreconditions().getAtoms();
        CodenotationConstraints temporaryCc = new CodenotationConstraints(new ArrayList<>(
                this.getCc().getCodenotations()));

        for (Atom precondition : stepPreconditions) {
            ContextualAtom preconditionInstance = new ContextualAtom(
                    step.getActionInstance().getContext(), precondition);

            if (!isAsserted(preconditionInstance, tc.getPrecedingSituation(step), temporaryCc)) {
                openConditions.add(buildOpenCondition(precondition, step));
            }
        }

        return openConditions;
    }

    /**
     * Retrieves all the threats regarding a given step
     *
     * @param toCheck : the step to check for threats
     * @return a list of all threats regarding the step to check.
     */
    private List<Flaw> getThreats(Step toCheck) {
        List<Flaw> threats = new ArrayList<>();

    toCheck.getActionPreconditions().getAtoms().forEach(precondition -> {
      ContextualAtom preconditionProposition = new ContextualAtom(
          toCheck.getActionInstance().getContext(), precondition);

            List<Step> destroyers = getDestroyers(
                    preconditionProposition,
                    tc.getPrecedingSituation(toCheck));

      for (Step destroyer : destroyers) {
        if (!isRestablished(preconditionProposition, destroyer, toCheck)) {
          threats.add(new Threat(
              destroyer,
              toCheck,
              tc.getPrecedingSituation(toCheck),
              preconditionProposition));
        }
      }
    });

        return threats;
    }

    /**
     * Checks if a given proposition is necessarily true in a given situation
     *
     * @param proposition : the proposition to check
     * @param situation   : the situation where it needs to be checked if it is
     *                    necessarily true
     * @return true if the proposition is necessarily true in the given situation,
     * false otherwise
     */
    public boolean isAsserted(
            ContextualAtom proposition,
            PopSituation situation) {
        return getEstablishers(proposition, situation).size() > 0;
    }

    public boolean isAsserted(
            ContextualAtom proposition,
            PopSituation situation,
            CodenotationConstraints cc) {
        return this.steps.stream()
                .filter(this::isNotFinalStep)
                .anyMatch(step -> !this.tc.isAfter(step, situation)
                        && step.assertsWithPermanentCodenotations(proposition, cc));
    }

  /**
   * Return all the steps which destroys (or threaten) a given proposition in a
   * given situation
   * 
   * @param proposition : the proposition we want to check
   * @param situation   : the situation in which we want to check if it has been
   *                    destroyed
   * @return the list of all steps which can destroy a given proposition in a
   *         given situation
   */
  public List<Step> getDestroyers(ContextualAtom proposition, PopSituation situation) {
    return this.steps.stream()
        .filter(step -> !this.tc.isAfter(step, situation))
        .filter(step -> step.destroys(proposition, this.getCc()))
        .collect(Collectors.toList());
  }

    /**
     * Get all the steps which establishes a given proposition in a given situation
     *
     * @param proposition : the proposition to establish
     * @param situation   : the situation in which the proposition needs to be
     *                    established
     * @return a list of steps which all establishes the proposition in the
     * situation
     */
    public List<Step> getEstablishers(ContextualAtom proposition, PopSituation situation) {
        return this.steps.stream()
                .filter(this::isNotFinalStep)
                .filter(step -> this.isBefore(this.tc.getFollowingSituation(step), situation))
                .filter(step -> step.asserts(proposition, this.getCc()))
                .collect(Collectors.toList());
    }

    /**
     * Builds an open condition based on which precondition is missing for a given
     * step in its
     * preceding situation
     *
     * @param missingPrecondition : the open condition which is not necessarily true
     * @param step                : the bearer of the open condition
     * @return an open condition stating the missing precondition for which step
     */
    protected OpenCondition buildOpenCondition(Atom missingPrecondition, Step step) {
        OpenCondition openCondition = new OpenCondition(
                tc.getPrecedingSituation(step),
                new ContextualAtom(step.getActionInstance().getContext(), missingPrecondition));
        return openCondition;
    }

    /**
     * Check if a given proposition is restablished between a destroyer and the
     * destroyed step.
     * Meaning there is a step `restablisher`, where destroyer < destroyed step, and
     * destroyer < restablisher < destroyed step
     *
     * @param proposition   : the proposition to check if it was restablished
     * @param destroyer     : the step which destroyed the proposition in the plan
     * @param destroyedStep : the step whose precondition was destroyed by the
     *                      destroyer step
     * @return true if the proposition is restablished, and false otherwise
     */
    public boolean isRestablished(ContextualAtom proposition, Step destroyer, Step destroyedStep) {
        return this.steps.stream()
                .filter(step -> step.asserts(proposition, this.getCc()))
                .filter(restablisher -> !restablisher.equals(destroyedStep)
                        && !restablisher.equals(destroyer))
                .anyMatch(restablisher -> tc.isBefore(destroyer, restablisher)
                        && tc.isBefore(restablisher, destroyedStep));
    }

    /**
     * Checks if a given proposition is restablished somewhere between the destroyer
     * step, and some
     * situation.
     *
     * @param proposition : the proposition to check
     * @param destroyer   : the step which destroyed the given proposition
     * @param situation   : the situation representing the limit where we want to
     *                    check for
     *                    restablishing steps
     * @return true if the given proposition is restablished by some other step `S`
     * where we have :
     * destroyer < S < situation.
     */
    public boolean isRestablished(
            ContextualAtom proposition,
            Step destroyer,
            PopSituation situation) {
        return this.steps.stream()
                .filter(step -> step.asserts(proposition, this.getCc()))
                .filter(restablisher -> !restablisher.equals(destroyer))
                .anyMatch(restablisher -> tc.isBefore(destroyer, restablisher)
                        && tc.isBefore(restablisher, situation));
    }

    /**
     * Checks if a given element is preceding the other element (either a step or a
     * situation).
     *
     * @param left  : the preceding element
     * @param right : the next element
     * @return true if left element is before the right element within the
     * partial-ordered plan.
     */
    public boolean isBefore(PlanElement left, PlanElement right) {
        return this.tc.isBefore(left, right);
    }

    /**
     * Explicit the temporal constraints of the plan by deleting any redundant
     * partial order in
     * the current plan.
     * <p>
     * For instance, a partial order may say that : A < D, and a few others states
     * that A < B,
     * B < C, and B < D. We can therefore delete A < D, as it can be deducted from
     * the other
     * temporal constraints which are way more explicit
     */
    public void removeRedundantTemporalConstraints() {
        this.getTc().refactorTemporalConstraints();
    }

    /**
     * Retrieves the initial step within the plan by using its reserved name.
     *
     * @return a step whose name is `initial`.
     * @see PlanInitializer for more details on dummy steps naming
     * convention as "initial" and "final"
     */
    public Step getInitialStep() {
        return this.steps.stream()
                .filter(step -> ((Action) step.getActionInstance().getLogicalEntity())
                        .getActionName()
                        .getName()
                        .equals(Keywords.POP_INITIAL_STEP.getName()))
                .findFirst()
                .orElse(null);
    }

    public Step getFinalStep() {
        return this.steps.stream()
                .filter(step -> ((Action) step.getActionInstance().getLogicalEntity())
                        .getActionName()
                        .getName()
                        .equals(Keywords.POP_FINAL_STEP.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the initial situation in the plan, that is the first situation
     * after the initial
     * step. This is mainly used for testing purposes.
     *
     * @return the situation right after the initial dummy step
     * @see settings.Keywords for the initial dummy step name.
     */
    public PopSituation getInitialSituation() {
        return this.tc.getFollowingSituation(this.getInitialStep());
    }

    public PopSituation getFinalSituation() {
        return this.tc.getPrecedingSituation(this.getFinalStep());
    }

    /**
     * Renders the graph of the plan into a png file. Name is based on the id to
     * differentiate them
     *
     * @throws IOException
     */
    public void renderAsGraphic(String outputName) throws IOException {
        String input = GraphvizGenerator.generateGraphviz(this);
        logger.info(input);
        String folderPath = "output";

        // Create the output folder if it doesn't exist
        Path outputPath = Paths.get(folderPath);
        Files.createDirectories(outputPath);

        // Save the DOT file
        Path dotFilePath = Paths.get(folderPath, outputName + ".dot");
        Files.write(dotFilePath, input.getBytes());
        Graphviz.fromString(input).render(Format.PNG).toFile(new File(folderPath, outputName));
    }

    /**
     * Formats the set of flaws so that each flaw in the flaw set occupies exactly one line. The next
     * flaw will be on a separate line and start with a tab.
     *
     * @param flaws
     * @return
     */
    private String formatFlaws(Set<Flaw> flaws) {
        String pattern = "\\)\\s*(?=\\w+:|\\[)"; // Lookahead for flaw start
        StringBuilder sb = new StringBuilder(); // Use StringBuilder for string modification

        for (Flaw flaw : flaws) {
            sb.append(flaw.toString()).append("\n\t"); // Append each flaw's String and a newline with tab
        }

        return sb.toString().trim(); // Remove any leading/trailing whitespace
    }


    private String formatList(String listToString) {
        if (listToString.equals("[]")) {
            return "NONE";
        }
        listToString = listToString
                .substring(1, listToString.length() - 1);

        return Arrays.stream(listToString.split(", "))
                .collect(Collectors.joining(",\n\t"));
    }

    @Override
    public String toString() {
        return "PLAN #" + this.id + "\n" +
                " - SITUATIONS : " + this.situations +
                "\n - STEPS : " + this.steps
                .stream()
                .map(step -> step.toStringWithCodenotations(this.cc))
                .toList()
                +
                "\n - CODENOTATIONS :\n" + "\t" + formatList(this.cc.toString()) +
                "\n - TEMPORAL CONSTRAINTS :\n\t" + this.tc +
                "\n - FLAWS :\n\t" +
                (this.flaws.isEmpty() ? "NONE" : formatFlaws(this.flaws));
    }

    @Override
    public String toGraphNode() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Flaw flaw : this.flaws) {
            stringBuilder.append(flaw);
        }
        return "(" + this.id + ") ";
    }
}
