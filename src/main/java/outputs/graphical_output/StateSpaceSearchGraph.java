package outputs.graphical_output;

import aStar.ProblemState;
import aStar_planning.pop.components.Plan;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public class StateSpaceSearchGraph {
    public static void renderAsGraphic_verbose(
            String outputFileName,
            List<ProblemState> allProblemStates,
            List<ProblemState> solutionProblemStates
    ) throws IOException {
        // Creates the input string for the dot file
        String input = StateSpaceSearchGraph.stateSpaceSearchDetails(
                allProblemStates,
                solutionProblemStates
        );

        // Configures in which folder the output is going to be created as a file
        String folderPath = "output";
        Path outputPath = Paths.get(folderPath);
        Files.createDirectories(outputPath);

        // Save the dot file
        Path dotFilePath = Paths.get(folderPath, outputFileName + ".dot");
        Files.write(dotFilePath, input.getBytes());
        Graphviz.fromString(input).render(Format.PNG).toFile(new File(folderPath,outputFileName));
    }

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

    /**
     * Builds the dotfile content to append all states to the dotfile of the state space search
     * @param allProblemStates : all the problem states explored
     * @param solutionProblemStates : the set of problem states which brings about the final state.
     * @return a String representing the input to build the graph.
     */
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
                appendAllOtherOptions(dotFileContent, problemState, allProblemStates);
                fillColorForFinalState(dotFileContent, problemState);
                fillColorForInitialState(dotFileContent, problemState);
            }

            problemStateSet.add(problemState);
        }

        return dotFileContent.toString();
    }

    /**
     * Appends all the other options to a dot file, excluding the problem state which is the chosen
     * next state
     * @param dotFileContent   : the dot file we want to append options to
     * @param solutionState    : the problem state that actually led to a solution, which we want to
     *                         exclude from the options to avoid redundancy.
     * @param allProblemStates : the set of all possible problem states explored during planning
     */
    private static void appendAllOtherOptions(
            StringBuilder dotFileContent,
            ProblemState solutionState,
            List<ProblemState> allProblemStates
    ) {
        List<ProblemState> otherOptionsList = allProblemStates.stream()
                .filter(problemState -> problemState.getParent().equals(solutionState.getParent()))
                .filter(problemState -> !problemState.equals(solutionState))
                .toList();

        for (int i = 0; i < otherOptionsList.size(); i++) {
            if (i >= GraphvizDisplaySettings.MAX_ALTERNATIVE_ARCS_PER_NODE){
                appendLeftoverOptionsAsOneNode(
                        dotFileContent,
                        solutionState,
                        GraphvizDisplaySettings.generateId(),
                        otherOptionsList.size() - i
                );
                break;
            }

            ProblemState otherOption = otherOptionsList.get(i);

            dotFileContent.append(String.format("\"%s\" -> \"%s\" [label=\"%s\"];\n",
                    solutionState.getParent().getState().toGraphNode(),
                    otherOption.getState().toGraphNode(),
                    otherOption.getAppliedOperator().toGraphArc())
            );

            fillColorForInvalidState(dotFileContent, otherOption);
        }
    }

    private static void appendLeftoverOptionsAsOneNode(
            StringBuilder dotFileContent,
            ProblemState solutionState,
            int id,
            int otherOptionsSize
    ) {
        dotFileContent.append(String.format("\"%s\" -> \"%s\";\n",
                solutionState.getParent().getState().toGraphNode(),
                "(" + id + ") And " + otherOptionsSize + " other option(s)."));
    }

    /**
     * Appends the color of the initial state inside the dot file at the top of the file
     * @param dotFileContent : the dot file we want to append the initial state color to
     * @param problemState : the problem state which contains the initial state.
     */
    private static void fillColorForInitialState(
            StringBuilder dotFileContent,
            ProblemState problemState
    ) {
        if (problemState.getParent().getParent() == null) {
            dotFileContent.insert(0,
                    String.format("\"%s\" [style=filled,fillcolor=\"%s\"];\n",
                            problemState.getParent().getState().toGraphNode(),
                            ColorTheme.INITIAL_STATE_FILLCOLOR
                    )
            );
        }
    }

    /**
     * Appends the color of the final state into the dot file.
     * @see ColorTheme for final state color
     * @param dotFileContent : the dotfile to which the color will be appended
     * @param problemState : the problem state which contains the final state.
     */
    private static void fillColorForFinalState(
            StringBuilder dotFileContent,
            ProblemState problemState
    ) {
        if (((Plan)problemState.getState()).getFlaws().isEmpty()) {
            dotFileContent.insert(0,
                    String.format("\"%s\" [style=filled,fillcolor=\"%s\"];\n",
                            problemState.getState().toGraphNode(),
                            ColorTheme.FINAL_STATE_FILLCOLOR
                    )
            );
        }
    }

    private static void fillColorForInvalidState(
            StringBuilder dotFileContent,
            ProblemState problemState
    ) {
        // TODO : make sure to call isValid instead of this.
        Plan plan = (Plan) problemState.getState();

        if (!plan.isCoherent()) {
            dotFileContent.insert(0,
                    String.format("\"%s\" [style=filled,fillcolor=\"%s\"];\n",
                            problemState.getState().toGraphNode(),
                            ColorTheme.INVALID_STATE_FILLCOLOR
                    )
            );
        }
    }
}
