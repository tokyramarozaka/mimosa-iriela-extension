package iriela;

import aStar.Operator;
import aStar_planning.pop.components.Flaw;
import aStar_planning.pop.components.PlanModification;
import aStar_planning.pop.components.Step;
import aStar_planning.pop_with_norms.NormativePlanningProblem;
import aStar_planning.pop_with_norms.components.Organization;
import aStar_planning.pop_with_norms.components.NormativePlan;
import iriela.description.PlanningProblemFactory;
import logic.Action;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Iriela_UT {
    private static final Logger logger = LogManager.getLogger(Iriela_UT.class);
    NormativePlanningProblem problem = PlanningProblemFactory.irielaProblem();

    @Test
    public void getActiveOrganizations_ok() {
        NormativePlan initialState = (NormativePlan) problem.getInitialState();

        List<Organization> organizations = initialState.getOrganizations();
        logger.info(initialState);
        assertEquals(organizations.size(), 4);
    }

    @Test
    public void evaluateNormativeFlaws_ok() {
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        Set<Flaw> initialFlaws = initialPlan.getFlaws();

        assertTrue(initialFlaws.size() == 2);
    }

    @Test
    public void getNormativeOptions_ok() {
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();
        List<Operator> options = problem.getOptions(initialPlan);

        logger.info(options);
        logger.debug("=".repeat(10) + ". Now showing the initial plan.");
        logger.info(initialPlan);
    }

    @Test
    public void applyOperatorSolvesFlaw_ok() {
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        List<Operator> options = problem.getOptions(initialPlan);
        PlanModification toApply = (PlanModification) options.get(0);
        initialPlan = (NormativePlan) initialPlan.applyPlanModification(toApply);
        Set<Flaw> nextFlaws = new HashSet<>(initialPlan.getFlaws());

        assertFalse(nextFlaws.contains(toApply.getTargetFlaw()));
    }

    @Test
    public void convertObligatoryPropositionsToGoals_ok() {
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();
        Step finalStep = initialPlan.getFinalStep();
        ActionPrecondition goals = finalStep.getActionPreconditions();

        List<Predicate> goalPredicates = goals.getAtoms()
                .stream()
                .map(Atom::getPredicate)
                .toList();

        assertTrue(goalPredicates
                .stream()
                .anyMatch(predicate -> predicate.getName().equals("haveFood"))
        );
        assertTrue(goalPredicates
                .stream()
                .anyMatch(predicate -> predicate.getName().equals("haveWood"))
        );
    }

    @Test
    public void permissionsAffectPossibleActions_ok(){
        NormativePlan initialPlan = (NormativePlan) problem.getInitialState();

        List<Action> permittedActions = initialPlan.getPermittedActions(
                initialPlan.getActionsFromAllInstitutions()
        );

        assertFalse(permittedActions.stream().anyMatch(action -> action.getActionName()
                .getName().equals("cut")));
    }
}
