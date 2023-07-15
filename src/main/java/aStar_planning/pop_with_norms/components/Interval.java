package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;

public interface Interval {
    PopSituation getBeginningSituation();
    PopSituation getEndingSituation();
}
