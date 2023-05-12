package aStar_planning.pop_with_norms.components;

import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.utils.NormsPerInterval;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import lombok.Getter;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NormativePlan extends Plan {
    private final List<Organization> organizations;
    private List<Organization> activeOrganizations;
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

    @Override
    public void evaluateFlaws() {
        super.evaluateFlaws();
        this.evaluateNormativeFlaws();
    }

    /**
     * Adds to the flaw list all flaws related to regulative norms and the situation in which they
     * are not applied, namely :
     * <ul>
     *     <li>Missing obligations</li>
     *     <li>Missing prohibitions</li>
     *     <li>Missing permissions</li>
     * </ul>
     */
    public void evaluateNormativeFlaws() {
        this.getSituations().forEach(situation -> this.getApplicableRegulativeNorms(situation)
                .forEach(applicableNorm -> {
                    if (!applicableNorm.isApplied(this, situation)) {
                        this.getFlaws().add(new NormativeFlaw(this,applicableNorm,situation));
                    }
                })
        );
    }

    /**
     * Evaluate all active organizations in a given situation.
     * @param situation : the situation we want to check all active organizations
     */
    public void evaluateActiveOrganizations(PopSituation situation){

    }
    
    /**
     * Returns a list of all regulative norms applicable to a given situation of the current plan
     * @param situation : the situation in the plan we want to check out for applicable norms
     * @return a list of regulative norms applicable to the given situation
     */
    private List<RegulativeNorm> getApplicableRegulativeNorms(PopSituation situation) {
        List<RegulativeNorm> applicableRegulativeNorms = new ArrayList<>();

        for (Organization organization : this.activeOrganizations) {
            List<RegulativeNorm> toAdd = organization.getRegulativeNormsByConcept(
                    Keywords.AGENT_CONCEPT
            );

            applicableRegulativeNorms.addAll(toAdd);
        }

        return applicableRegulativeNorms;
    }

    @Override
    public String toString() {
        String applicableNormsAsString = "\nApplicable norms:\n\t" + this.getNormsPerIntervals();

        return super.toString().concat(applicableNormsAsString);
    }
}