package outputs.graphical_output;

import aStar.AStarPlanner;
import aStar.AStarProblem;
import aStar.Operator;
import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class StateSpaceSearchGraph {

    public static String stateSpaceSearchDetails(State initialState, List<Operator> operators,
                                                 AStarPlanner planner) {
        StringBuilder dotFileContent = new StringBuilder("digraph G {\n");

        // Initialize the state space search starting from the initial state
        stateSpaceSearchHelper(initialState, operators, dotFileContent, planner);

        dotFileContent.append("}\n");

        return dotFileContent.toString();
    }

    private static void stateSpaceSearchHelper(
            State currentState,
            List<Operator> operators,
            StringBuilder dotFileContent,
            AStarPlanner planner
    ) {
        Map<State, List<Operator>> statesAndTheirOptions = planner.getOptionsHistory();

        statesAndTheirOptions.entrySet()
                .stream()
                .filter(stateListEntry -> {
                    state
                });

        for (Operator option : options) {
            if (operators.contains(option)) {

            }
            // Apply the operator to get the next state
            State nextState = planner.getProblem().apply(option, currentState);

            // Generate the link between the current state and its next state with a label
            dotFileContent.append(String.format("\"%s\" -> \"%s\" [label=\"%s\"];\n",
                    currentState.toGraphNode(),
                    nextState.toGraphNode(),
                    option.toGraphArc())
            );
        }
    }
}
