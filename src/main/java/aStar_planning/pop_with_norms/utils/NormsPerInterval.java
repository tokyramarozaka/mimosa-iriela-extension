package aStar_planning.pop_with_norms.utils;

import aStar_planning.pop_with_norms.components.norms.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.norms.Norm;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import time.Interval;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of norms attached to a given interval of situations
 */
@AllArgsConstructor
@Getter
@ToString
public class NormsPerInterval {
    private Interval interval;
    private List<Norm> norms;

    public List<RegulativeNorm> getRegulativeNorms() {
        return this.norms.stream()
                .filter(norm -> norm instanceof RegulativeNorm)
                .map(norm -> (RegulativeNorm) norm)
                .collect(Collectors.toList());
    }

    public NormsPerInterval getRegulativeNormsWithInterval() {
        return new NormsPerInterval(
                this.interval,
                this.norms.stream()
                        .filter(norm -> norm instanceof RegulativeNorm)
                        .map(norm -> (RegulativeNorm) norm)
                        .collect(Collectors.toList())
        );
    }

    public List<ConstitutiveNorm> getConstitutiveNorms() {
        return this.norms.stream()
                .filter(norm -> norm instanceof ConstitutiveNorm)
                .map(norm -> (ConstitutiveNorm) norm)
                .collect(Collectors.toList());
    }

    public NormsPerInterval getConstitutiveNormsWithInterval() {
        return new NormsPerInterval(
                this.interval,
                this.norms.stream()
                        .filter(norm -> norm instanceof ConstitutiveNorm)
                        .map(norm -> (ConstitutiveNorm) norm)
                        .collect(Collectors.toList())
        );
    }
}
