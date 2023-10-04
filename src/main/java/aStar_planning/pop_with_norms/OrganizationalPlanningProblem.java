package aStar_planning.pop_with_norms;

import aStar.Operator;
import aStar.State;
import aStar_planning.pop.PopPlanningProblem;
import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.utils.PlanInitializer;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Action;
import logic.Goal;
import logic.Situation;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class OrganizationalPlanningProblem extends PopPlanningProblem {
    private final NormativePlan initialNormativePlan;
    private final List<Organization> organizations;

    private final Logger logger = LogManager.getLogger(OrganizationalPlanningProblem.class);

    public OrganizationalPlanningProblem(
            Situation initialSituation,
            List<Action> possibleActions,
            Goal goal,
            List<Organization> organizations
    ){
        super(initialSituation, possibleActions, goal, true);
        this.organizations = organizations;
        this.initialNormativePlan = PlanInitializer.constructInitialPlan(
                initialSituation,
                goal,
                organizations
        );
        this.getPossibleActions().addAll(this.initialNormativePlan.getActionsFromAllInstitutions());
    }

    @Override
    public State getInitialState() {
        return this.initialNormativePlan;
    }

    @Override
    public boolean isFinal(State state) {
        boolean isFinal = super.isFinal(state);
        NormativePlan plan = (NormativePlan) state;

        for (PopSituation situation : plan.getSituations()) {
            logger.debug("All asserted propositions in : " + situation + " are : " +
                    plan.getAllAssertedPropositions(situation)
                            .stream()
                            .filter(proposition -> !proposition.getAtom().getPredicate().getName()
                                    .equals("areAdjacents"))
                            .collect(Collectors.toList()));
        }

        return isFinal;
    }
}
