package aStar_planning.pop;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.Context;
import logic.ContextualAtom;
import logic.ContextualPredicate;
import logic.Goal;
import logic.LogicalInstance;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import planning.Problem;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class PopPlanningProblem extends Problem implements AStarProblem{
    private Plan initialPlan;

    public PopPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                              Goal goal){
        super(initialSituation, possibleActions, goal);
        buildInitialPlan();
    }

    private void buildInitialPlan() {
        List<PopSituation> initialAndFinalSituations = this.buildInitialSituations();
        List<Step> initialAndFinalSteps = this.buildDummySteps();

        CodenotationConstraints initialCodenotationConstraints = new CodenotationConstraints();
        TemporalConstraints initialTemporalConstraints = this
                .buildInitialTemporalConstraints(initialAndFinalSituations, initialAndFinalSteps);

        this.initialPlan = new Plan(initialAndFinalSituations, initialAndFinalSteps,
                initialCodenotationConstraints, initialTemporalConstraints);
    }

    private TemporalConstraints buildInitialTemporalConstraints(List<PopSituation> situations,
                                                                List<Step> steps) {
        List<PartialOrder> partialOrders = new ArrayList<>();

        PopSituation initialSituation = situations.get(0);
        PopSituation finalSituation = situations.get(1);
        Step initialStep = steps.get(0);
        Step finalStep = steps.get(1);

        partialOrders.add(new PartialOrder(initialStep, initialSituation));
        partialOrders.add(new PartialOrder(initialSituation, finalSituation));
        partialOrders.add(new PartialOrder(finalSituation, finalStep));

        return new TemporalConstraints(partialOrders);
    }

    private List<PopSituation> buildInitialSituations(){
        PopSituation initialSituation = new PopSituation(), finalSituation = new PopSituation();

        List<PopSituation> dummySituations = new ArrayList<>();
        dummySituations.add(initialSituation);
        dummySituations.add(finalSituation);

        return dummySituations;
    }

    private List<Step> buildDummySteps(){
        List<Step> dummySteps = new ArrayList<>();

        LogicalInstance initialStep = new LogicalInstance(
                this.producingAction(this.getInitialSituation().getContextualPredicates(),
                        "initial"),
                new Context()
        );

        LogicalInstance finalStep = new LogicalInstance(
                this.requiringAction(this.getGoal().getPropositions(), "final"),
                new Context()
        );

        return dummySteps;
    }

    public Action producingAction(List<ContextualPredicate> propositions, String actionName){
        return new Action(
                actionName,
                new ActionPrecondition(),
                new ActionConsequence(propositions
                        .stream()
                        .map(proposition -> new Atom(false, proposition.getPredicate()))
                        .toList()
                )
        );
    }

    public Action requiringAction(List<ContextualAtom> propositions, String actionName){
        return new Action(
                actionName,
                new ActionPrecondition(propositions
                        .stream()
                        .map(proposition -> proposition.getAtom())
                        .toList()),
                new ActionConsequence()
        );
    }
    @Override
    public State getInitialState() {
        return this.initialPlan;
    }

    @Override
    public boolean isFinal(State state) {
        return ((Plan)state).isExecutable();
    }

    @Override
    public List<Operator> getOptions(State state) {
        return ((Plan)state).possibleModifications();
    }

    @Override
    public State apply(Operator operator, State state) {
        return ((Plan)state).applyPlanModification((PlanModification)operator);
    }

    @Override
    public double evaluateState(State state) {
        return ((Plan)state).getFlaws().size();
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 0;
    }

    @Override
    public boolean isValid(State state) {
        return ((Plan)state).isCoherent();
    }

    @Override
    public List<Operator> getSolution(List<Operator> solutionSteps) {
        return null;
    }

    @Override
    public String showSolution(List<Operator> solutionSteps) {
        return null;
    }
}
