package aStar_planning.pop_with_norms.components;

import aStar.State;
import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Step;
import constraints.CodenotationConstraints;
import constraints.TemporalConstraints;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class OrganizationalPlan extends Plan {
    private final List<Organization> organizations;

    public OrganizationalPlan(
            List<PopSituation> situations,
            List<Step> steps,
            CodenotationConstraints cc,
            TemporalConstraints tc,
            List<Organization> organizations
    ){
        super(situations, steps, cc, tc);
        this.organizations = organizations;
    }
}