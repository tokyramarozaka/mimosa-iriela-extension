package aStar;

import aStar_planning.pop.components.Plan;
import exception.NoPlanFoundException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import outputs.PlanningOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Getter
public class AStarPlanner {
    private final static Logger logger = LogManager.getLogger(AStarPlanner.class);
    private final Queue<ProblemState> open = new PriorityQueue<>();
    private final List<State> closed = new ArrayList<>();
    private AStarProblem problem;
    private ProblemState finalProblemState;

    public AStarPlanner(AStarProblem problem) {
        this.problem = problem;
    }

    /**
     * Returns the list of operators which would allow to go from the initial state to a final state
     *
     * @return the list of Operators in a given order to reach a final state (if any).
     * @throws NoPlanFoundException : no final state was found
     */
    public List<Operator> findSolution() throws NoPlanFoundException {
        open.add(new ProblemState(
                null,
                problem.getInitialState(),
                null,
                0,
                problem.evaluateState(problem.getInitialState())
        ));

        boolean found = false;
        ProblemState solutionState = null;

        while (!open.isEmpty() && !found) {
            ProblemState candidate = open.poll();

            if (closed.contains(candidate.getState())) {
                continue;
            }
            
            for (Operator option : problem.getOptions(candidate.getState())) {
                State nextState = problem.apply(option, candidate.getState());

                ProblemState successor = new ProblemState(
                        candidate, nextState,
                        option,
                        candidate.g + problem.evaluateOperator(option),
                        problem.evaluateState(nextState)
                );

                if (problem.isValid(nextState)) {
                    if (problem.isFinal(nextState)) {
                        found = true;
                        solutionState = successor;
                        break;
                    } else {
                        open.add(successor);
                    }
                }
            }

            closed.add(candidate.getState());
        }

        if (solutionState == null) {
            throw new NoPlanFoundException();
        }

        this.finalProblemState = solutionState;
        List<Operator> solution = extractSolution(solutionState);

        return solution;
    }

    /**
     * Returns the set of all operators that have been applied given a final State. By using its
     * encapsulating ProblemState. We can trace back all the Operators that have been applied,
     * starting from the last to the first Operator.
     * Depending on the type of technique used to solve the problem (Forward, Backward) this order
     * might need to be reversed to obtain the set of operators in the right order.
     *
     * @param finalState : the final ProblemState which will allow to get back to its parent
     * @return the set of applied operator in the right order : from the first to the last.
     */
    public List<Operator> extractSolution(ProblemState finalState) {
        List<Operator> solution = new ArrayList<>();

        ProblemState iteratorFromFinalState = finalState;

        while (iteratorFromFinalState.getParent() != null) {
            solution.add(iteratorFromFinalState.getAppliedOperator());
            iteratorFromFinalState = iteratorFromFinalState.getParent();
        }

        return problem.getSolution(solution);
    }

    /**
     * Outputs the final plan to be agent. Since it can be a partial-order plan, or simply a set of
     * totally ordered actions, or any sort of eventual other representation it simply returns an
     * object that can be considered as a Plan.
     * @return
     */
    public PlanningOutput outputSolutionPlan() throws NoPlanFoundException, IOException {
        List<Operator> operators = findSolution();
        State finalState = this.getFinalProblemState().getState();

        Plan plan = (Plan) finalState;

        plan.render("overview.png");
        PlanningOutput output = problem.outputPlan(finalState, operators);

        plan.render("simplified.png");

        return output;
    }

}
