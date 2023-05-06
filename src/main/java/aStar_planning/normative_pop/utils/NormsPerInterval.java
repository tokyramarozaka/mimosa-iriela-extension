package aStar_planning.normative_pop.utils;

import aStar_planning.normative_pop.components.norms.Norm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import time.Interval;

import java.util.List;

/**
 * A set of norms attached to a given interval of situations
 */
@AllArgsConstructor
@Getter
@ToString
public class NormsPerInterval {
    private Interval interval;
    private List<Norm> norms;
}
