package aStar_planning.pop_with_norms.components;

import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import aStar_planning.pop_with_norms.utils.NormsPerInterval;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import logic.ContextualAtom;
import lombok.Getter;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NormativePlan extends Plan {
    private final List<Organization> organizations;
    private final List<NormsPerInterval> normsPerIntervals;
    private List<Organization> activeOrganizations;

    public NormativePlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations,
            List<NormsPerInterval> normsPerIntervals
    ) {
        super(situations, steps, cc, tc);
        this.organizations = organizations;
        this.evaluateActiveOrganizations();
        this.normsPerIntervals = normsPerIntervals;
        this.evaluateNormativeFlaws();
    }

    public NormativePlan(
            Plan plan,
            List<Organization> organizations,
            List<NormsPerInterval> normsPerIntervals
    ) {
        super(plan.getSituations(), plan.getSteps(), plan.getCc(), plan.getTc());
        this.organizations = organizations;
        this.evaluateActiveOrganizations();
        this.normsPerIntervals = normsPerIntervals;
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
                        this.getFlaws().add(new NormativeFlaw(this, applicableNorm, situation));
                    }
                })
        );
    }

    /**
     * Evaluate all active organizations in the plan and updates its list in the plan.
     */
    public void evaluateActiveOrganizations() {
        this.activeOrganizations = new ArrayList<>();

        this.getOrganizations().forEach(organization -> {
            if (organization.hasRole(Keywords.AGENT_CONCEPT)) {
                this.activeOrganizations.add(organization);
            }
        });
    }

    /**
     * Returns a list of all regulative norms applicable to a given situation of the current plan
     *
     * @param situation : the situation in the plan we want to check out for applicable norms
     * @return a list of regulative norms applicable to the given situation
     */
    private List<RegulativeNorm> getApplicableRegulativeNorms(PopSituation situation) {
        List<RegulativeNorm> applicableRegulativeNorms = new ArrayList<>();

        for (Organization organization : this.activeOrganizations) {
            List<RegulativeNorm> allOrganizationNorms = organization.getRegulativeNormsByConcept(
                    Keywords.AGENT_CONCEPT
            );

            List<RegulativeNorm> toAdd = allOrganizationNorms.stream()
                    .filter(norm -> hasAnyApplicabilityCodenotations(situation, norm))
                    .toList();

            applicableRegulativeNorms.addAll(toAdd);
        }

        return applicableRegulativeNorms;
    }

    private boolean hasAnyApplicabilityCodenotations(PopSituation situation, RegulativeNorm norm) {
        try {
            norm.getNormConditions().getApplicableCodenotations(this, situation);

            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }


    /**
     * Returns all the asserted proposition in a given situation of the plan by retrieving all
     * preceding steps and their consequences
     *
     * @param situation : the situation where we want to retrieve all asserted propositions from
     * @return a list of propositions i.e. of ContextualAtoms which are
     */
    public List<ContextualAtom> getAllAssertedPropositions(
            PopSituation situation
    ) {
        List<ContextualAtom> assertedPropositions = new ArrayList<>();

        this.getSteps().stream()
                .filter(step -> this.getTc().isBefore(step, situation))
                .forEach(precedingStep -> {
                    System.out.println("preceding step " + precedingStep + " of " + situation);
                    precedingStep.getActionConsequences().getAtoms().forEach(consequence -> {
                        ContextualAtom toAdd = new ContextualAtom(
                                precedingStep.getActionInstance().getContext(), consequence
                        );

                        if (this.isAsserted(toAdd, situation)) {
                            assertedPropositions.add(toAdd);
                        }
                    });
                });

        return assertedPropositions;
    }

    @Override
    public String toString() {
        return super.toString().concat(
                "\n--INSTITUTIONS\n" + this.activeOrganizations.toString()
        );
    }
}
