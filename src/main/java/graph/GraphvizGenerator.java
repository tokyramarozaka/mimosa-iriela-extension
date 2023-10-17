package graph;

import aStar_planning.pop.components.Step;

public class GraphvizGenerator {
    public static String generateGraphviz(Graph graph) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");

        for (Node node : graph.getNodes()) {
            for (Link link : node.getLinks()){
                dot.append(String.format(
                        "  \"%s\" -> \"%s\";\n",
                        node.getContent().toString(),
                        link.getTo().getContent().toString())
                );
            }
        }

        dot.append("}\n");

        return dot.toString();
    }

}