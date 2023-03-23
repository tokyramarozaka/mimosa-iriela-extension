package aStar;

import outputs.PlanningOutput;

import java.util.List;

/**
 * Defines an A* star problem and the essential methods that must be detailed to solve it as a
 * State-space search.
 *
 * @author Tokimahery Ramarozaka
 */
public interface AStarProblem {
    /**
     * Provides a mean to get the initial State which is the first state we are starting our state-
     * space search
     * @return the initial State of the Problem
     */
    State getInitialState();


    /**
     * Checks if a State is final or not, meaning that the State resolves the planning problem
     * @param state
     * @return
     */
    boolean isFinal(State state);

    /**
     * Returns a list of all possible Operators in a given State, this allows us to branch one State
     * into new ones and keep exploring the State-space.
     * @param state : the State we want to branch out
     * @return the set of applicable operators on the given State
     */
    List<Operator> getOptions(State state);

    /**
     * Apply an operator to a State and returns the next State
     * @param operator : the operator to apply
     * @param state : the state to apply the operator to
     * @return the next State after the Operator has been applied
     */
    State apply(Operator operator, State state);

    /**
     * Returns a double indicating the distance separating a State from being a final State.The
     * lower the distance, the closer it is. And the sooner it will be explored. In other words:
     * "less is better". This ensures that we explore the most promising States first.
     * @param state : the state to be evaluated
     * @return the distance of the State from being a final State.
     */
    double evaluateState(State state);

    /**
     * Returns the cost of an Operator, meaning that applying certain operators might cost more
     * than others. The lesser the cost, the better. This ensures that we explore the less costly
     * States first
     *
     * @param operator : the operator to be evaluated
     * @return a double indicating the total cost of applying the given Operator
     */
    double evaluateOperator(Operator operator);

    /**
     * Determines if a State is valid. In the case it is invalid, it is thrown out of the search space
     * @param state
     * @return
     */
     boolean isValid(State state);

    /**
     * Determines the correct order of the transitions of the solutions
     * Since transitions are added progressively throughout the search, i.e. last transition is on
     * top, first one is at the bottom. It is in some cases (Forward, Pop...) necessary to return to
     * the first added transition by reversing the list of transitions
     *
     * @param solutionOperators : the solution operators, first transition is at the bottom,
     *                          last step is on top.
     * @return the set of ordered Operators that attains the desired State
     */
     List<Operator> getSolution(List<Operator> solutionOperators);

    /**
     * Displays the solution in a convenient formatted String
     * @param finalState: the final State attained after the state space search
     * @param solutionOperators: the operators used during state space search
     * @return the representation of the plan that can be deducted.
     */
    PlanningOutput outputPlan(State finalState, List<Operator> solutionOperators);
}
