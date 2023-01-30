package aStar_planning.pop.components;

import graph.Node;

/**
 * A simple interface to generalize elements of a plan which are situations and steps
 * This is needed when using temporal constraints on both steps and situations
 */
public interface PlanElement {
    Node toNode();
}
