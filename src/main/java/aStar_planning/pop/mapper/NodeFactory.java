package aStar_planning.pop.mapper;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
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
