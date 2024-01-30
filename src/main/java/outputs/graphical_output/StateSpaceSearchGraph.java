package outputs.graphical_output;

import aStar.ProblemState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class StateSpaceSearchGraph {

    public static String stateSpaceSearchDetails(
            List<ProblemState> allProblemStates,
            List<ProblemState> solutionProblemStates
    ) {
        StringBuilder dotFileContent = new StringBuilder("digraph G {\n");

        // Initialize the state space search starting from the initial state
        dotFileContent.append(getDotFileContent(allProblemStates, solutionProblemStates));

        dotFileContent.append("}\n");

        return dotFileContent.toString();
    }

    private static String getDotFileContent(
            List<ProblemState> allProblemStates,
            List<ProblemState> solutionProblemStates
    ) {
        StringBuilder dotFileContent = new StringBuilder();
        Set<ProblemState> problemStateSet = new HashSet<>();

        for (ProblemState problemState : allProblemStates) {
            if (problemStateSet.contains(problemState)) {
                continue;
            }

            if (solutionProblemStates.contains(problemState)) {
                dotFileContent.append(String.format("\"%s\" -> \"%s\" [label=\"%s\"];\n",
                        problemState.getParent().getState().toGraphNode(),
                        problemState.getState().toGraphNode(),
                        problemState.getAppliedOperator().toGraphArc())
                );
                insertFillColorIfFinalState(dotFileContent, problemState);
                insertFillColorIfInitialState(dotFileContent, problemState);
            }

            problemStateSet.add(problemState);
        }

        return dotFileContent.toString();
    }

    private static void insertFillColorIfInitialState(StringBuilder dotFileContent, ProblemState problemState) {
        if (problemState.getParent().getParent() == null) {
            dotFileContent.insert(0,
                    String.format("\"%s\" [style=filled,fillcolor=\"lightgreen\"];\n",
                            problemState.getParent().getState().toGraphNode()
                    )
            );
        }
    }

    private static void insertFillColorIfFinalState(StringBuilder dotFileContent, ProblemState problemState) {
        if(problemState.getState().toGraphNode().isBlank()){
            dotFileContent.insert(0,
                    String.format("\"%s\" [style=filled,fillcolor=\"red\"];\n",
                            problemState.getState().toGraphNode()
                    )
            );
        }
    }
}
