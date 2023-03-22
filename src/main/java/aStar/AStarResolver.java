package aStar;

import exception.NoPlanFoundException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Getter
public class AStarResolver {
    private AStarProblem problem;
    private final Queue<ProblemState> open = new PriorityQueue<>();
    private final List<State> closed = new ArrayList<>();
    private final static Logger logger = LogManager.getLogger(AStarResolver.class);

    public AStarResolver(AStarProblem problem){
        this.problem = problem;
    }

    /**
     * Returns the list of operators which would allow to go from one state to a final state
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

            if(closed.contains(candidate.getState())) {
                continue;
            }

            if (problem.isFinal(candidate.getState())){
                solutionState = candidate;
                break;
            }
            
            for (Operator option : problem.getOptions(candidate.getState())) {
                State nextState = problem.apply(option, candidate.getState());

                ProblemState successor = new ProblemState(
                        candidate,nextState,
                        option,
                        candidate.g + problem.evaluateOperator(option),
                        problem.evaluateState(nextState)
                );

                if(problem.isValid(nextState)) {
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

        List<Operator> solution = extractSolution(solutionState);

        return solution;
    }

    public List<Operator> extractSolution(ProblemState finalState){
        List<Operator> solution = new ArrayList<>();

        ProblemState iteratorFromFinalState = finalState;

        while (iteratorFromFinalState.getParent() != null) {
            solution.add((Operator) iteratorFromFinalState.getAppliedOperator());
            iteratorFromFinalState = iteratorFromFinalState.getParent();
        }

        return problem.getSolution(solution);
    }
}
