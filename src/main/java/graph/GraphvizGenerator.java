package graph;

import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphvizGenerator {
    /**
     * Get all the first following steps for a given situation, i.e. it searches for steps within
     * a given String representation of the temporal constraints, starting from a given
     * PopSituation.
     *
     * @param source : the PopSituation from which we are starting our research for a step
     * @param input  : the set of all temporal constraints
     * @return all steps directly following the source
     */
    public static List<String> getFollowingSteps(
            String source,
            List<String> input
    ) {
        List<String> followingSteps = new ArrayList<>();
        String finalSource = source.substring(0, source.length() - 1);

        input.stream()
                .filter(line -> line.split(" -> ")[0].contains(finalSource))
                .forEach(line -> {
                    String right = line.split(" -> ")[1];
                    if (isPopSituation(right)) {
                        followingSteps.addAll(getFollowingSteps(right, input));
                    } else {
                        followingSteps.add(right);
                    }
                });

        return followingSteps;
    }

    /**
     * Removes all situations within the graph. Making the plan much more readable
     * @param input : the initial string used to generate the graph with situations
     * @return a situation-less plan with only the action instances(steps) left.
     */
    public static String removeSituations(String input) {
        StringBuilder result = new StringBuilder();
        List<String> lines = Arrays.asList(input.split("\n"));

        result.append("digraph G { \n");

        for (int i = 1; i < lines.size() - 1; i++) {
            String[] order = lines.get(i).split(" -> ");
            String left = order[0];
            String right = order[1];

            if (!isPopSituation(left)) {
                for (String followingStep : getFollowingSteps(right, lines)) {
                    result
                            .append(left)
                            .append(" -> ")
                            .append(followingStep)
                            .append("\n");
                }
            }
        }

        result.append("}");

        return result.toString();
    }

    private static boolean isPopSituation(String left) {
        return left.contains("PopSituation");
    }

    public static String generateGraphviz(Plan plan) {
        StringBuilder dot = new StringBuilder();
        Graph graph = plan.getTc().getGraph();
        dot.append("digraph G {\n");

        for (Node node : graph.getNodes()) {
            for (Link link : node.getLinks()) {
                String left = buildPlanElement(node.getContent(), plan);
                String right = buildPlanElement(link.getTo().getContent(), plan);
                dot.append(String.format("  \"%s\" -> \"%s\";\n", left, right));
            }
        }

        dot.append("}\n");

        return removeSituations(dot.toString());
    }

    public static String buildPlanElement(Object planElement, Plan plan) {
        if (planElement instanceof Step step) {
            return step.toStringWithCodenotations(plan.getCc());
        } else if (planElement instanceof PopSituation popSituation) {
            return popSituation.toString();
        } else {
            return planElement.toString();
        }
    }
}