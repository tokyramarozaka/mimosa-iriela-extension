package aStar;

import java.util.List;

/**
 * Defines an A* star problem
 * and the essential methods of a problem
 * @author Tokimahery
 *
 */
public interface AStarProblem {
    /**
     * Provides a mean to get the starting point we are coming from
     * @return the inital State of the Problem
     */
    public State getInitialState();


    /**
     * Checks if a State is final or not
     * @param state
     * @return
     */
    public boolean isFinal(State state);

    /**
     * Returns a list of all possible transitions in a given state
     * @param state
     * @return
     */
    public List<Operator> getOptions(State state);

    /**
     * Apply a operator to a State and returns the resulting State
     * @param operator : the operator to apply
     * @param state : the state to apply the operator to
     * @return
     */
    public State apply(Operator operator, State state);

    /**
     * Returns a double indicating the distance between a State and the goal
     * The lower the distance, the closer it is,
     * And the sooner it will be considered in the planning process
     * @param state : the state to be evaluated
     * @return
     */
    public double evaluateState(State state);

    /**
     * Evaluates the cost of an operator
     * It is used in processing which ProblemState in A* is processed first alongside heuristic distance
     * @param operator
     * @return
     */
    public double evaluateOperator(Operator operator);

    /**
     * Determines if a state is valid. In the case it is invalid, it is thrown out of the search space
     * @param state
     * @return
     */
    public boolean isValid(State state);

    /**
     * Determines the correct order of the transitions of the solutions
     * Since transitions are added progressively throughout the search, i.e. last transition is on top, first one is at the bottom,
     * It is in some cases (Forward, Pop...) necessary to return to the first added transition by reversing the list of transitions
     * @param solutionSteps : the raw transitions of the planning, first transition is at the bottom, last step is on top.
     * @return the set of ordered Transitions that attains the desired State
     */
    public List<Operator> getSolution(List<Operator> solutionSteps);

    /**
     * Displays the solution in a convenient formatted String
     * @param solutionSteps
     * @return a set of operators in a readable way
     */
    public String showSolution(List<Operator> solutionSteps);
}
