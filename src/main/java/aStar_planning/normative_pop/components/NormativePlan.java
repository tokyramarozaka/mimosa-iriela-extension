package aStar_planning.normative_pop.components;

import aStar_planning.normative_pop.components.norms.Norm;
import aStar_planning.normative_pop.utils.NormsPerInterval;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class NormativePlan extends Plan {
    private final List<Organization> organizations;
    private final List<NormsPerInterval> normsPerIntervals;

    public NormativePlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations,
            List<NormsPerInterval> normsPerInterval
    ) {
        super(situations, steps, cc, tc);
        this.organizations = organizations;
        this.normsPerIntervals = normsPerInterval;
    }

    /**
     * Get the list of all applicable norms in a given situation by checking if it is within
     * some interval that endows norms.
     * @param situation : the situation we want to compute all norms from.
     * @return the list of all applicable norms applicable in the given situation
     */
    public List<Norm> getApplicableNorms(PopSituation situation) {
        List<Norm> applicableNorms = new ArrayList<>();

        this.normsPerIntervals.forEach(intervalWithNorms -> {
            if (intervalWithNorms.getInterval().contains(situation)) {
                applicableNorms.addAll(intervalWithNorms.getNorms());
            }
        });

        return applicableNorms;
    }

    @Override
    public void evaluateFlaws() {
        super.evaluateFlaws();
        // TODO : this.evaluateNormativeFlaws();
    }

    /**
     * TODO : detect all normative flaws within the plan
     * Updates the flaw set by adding in the flaws related to norms
     */
//    private void evaluateNormativeFlaws() {
//        this.getSituations().forEach(situation -> this.getApplicableNorms(situation)
//                .forEach(applicableNorm -> {
//                    if (!applicableNorm.isApplied(situation)) {
//                        this.getFlaws().add(new NormativeFlaw(this,applicableNorm,interval));
//                    }
//                })
//        );
//    }

    @Override
    public String toString() {
        String applicableNormsAsString = "\nApplicable norms:\n\t" + this.getNormsPerIntervals();

        return super.toString().concat(applicableNormsAsString);
    }
}
