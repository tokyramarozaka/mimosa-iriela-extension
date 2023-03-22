package graph.aStar_planning.pop.mapper;

import graph.aStar_planning.pop.utils.components.PopSituation;
import graph.aStar_planning.pop.utils.components.Step;
import graph.Node;

import java.util.ArrayList;

public class NodeFactory {
    public static Node stepToNode(Step toMap){
        return new Node(toMap.toString(), new ArrayList<>(), toMap);
    }

    public static Node situationToNode(PopSituation toMap){
        return new Node(toMap.toString(), new ArrayList<>(), toMap);
    }
}
