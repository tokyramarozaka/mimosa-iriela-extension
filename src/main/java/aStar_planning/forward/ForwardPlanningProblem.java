package aStar_planning.forward;

import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.Goal;
import logic.LogicalInstance;
import logic.Situation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import planning.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForwardPlanningProblem extends Problem implements AStarProblem {
    private static final Logger logger = LogManager.getLogger(ForwardPlanningProblem.class);

    public ForwardPlanningProblem(Situation initialSituation, List<Action> possibleActions,
                                  Goal goal) {
        super(initialSituation, possibleActions, goal);
    }

    @Override
    public State getInitialState() {
        return this.getInitialSituation();
    }

    @Override
    public boolean isFinal(State state) {
        return ((Situation)state).satisfies(this.getGoal());
    }

    @Override
    public List<Operator> getOptions(State state) {
        Situation currentSituation = ((Situation)state);
        List<Operator> options = new ArrayList<>();

        currentSituation
                .allPossibleActionInstances(this.getPossibleActions())
                .forEach(possibleActionInstance -> options.add(possibleActionInstance));

        logger.info("GETTING OPTIONS FOR : \n\t"+state+"\nOPTIONS ARE : \n\t"+options);
        return options;
    }

    @Override
    public State apply(Operator operator, State state) {
        logger.info("APPLYING "+operator);
        logger.info("GOT :"+((Situation)state).applyActionInstance((LogicalInstance)operator));
        return ((Situation)state).applyActionInstance((LogicalInstance)operator);
    }

    @Override
    public double evaluateState(State state) {
        logger.info("Heuristic distance : "+((Situation)state).goalDistance(this.getGoal()));
        return ((Situation)state).goalDistance(this.getGoal());
    }

    @Override
    public double evaluateOperator(Operator transition) {
        return 1;
    }

    @Override
    public boolean isValid(State state) {
        return true;
    }

    @Override
    public List<Operator> getSolution(List<Operator> solutionOperators) {
        Collections.reverse(solutionOperators);
        return solutionOperators;
    }

    @Override
    public String showSolution(List<Operator> solutionPlan) {
        StringBuilder stringBuilder = new StringBuilder();

        solutionPlan.forEach(step -> {
            stringBuilder.append("\n\t").append(step);
        });

        return stringBuilder.toString();
    }
}
