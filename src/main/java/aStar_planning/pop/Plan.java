package aStar_planning.pop;

import aStar.Operator;
import aStar.State;
import logic.Action;
import logic.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Plan implements State {
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints codenotationConstraints;
    private TemporalConstraints temporalConstraints;
    private Set<Flaw> flaws;

    public Plan(List<PopSituation> situations, List<Step> steps, CodenotationConstraints codenotationConstraints, TemporalConstraints temporalConstraints) {
        this.situations = situations;
        this.steps = steps;
        this.codenotationConstraints = codenotationConstraints;
        this.temporalConstraints = temporalConstraints;
        this.flaws = new HashSet<>();
    }

    public void evaluateFlaws(){
        this.flaws = new HashSet<>();

        for(Step step : this.steps.stream().filter(this::isNotInitialStep)) {
            this.getThreats(step)
                    .forEach(threat -> this.flaws.add(threat));

            this.getOpenConditions(step)
                    .forEach(openCondition -> this.flaws.add(openCondition));
        }
    }

    private boolean isNotInitialStep(Step step) {
        Action stepAction = ((Action) step.getActionInstance().getLogicalEntity());

        return !Objects.equals(
                stepAction.getName(),
                "initial"
        );
    }

    private List<Flaw> getOpenConditions(Step step) {

    }

    private List<Flaw> getThreats(Step step) {
        
    }

    public List<Operator> possibleModifications() {
        List<Operator> possibleModifications = new ArrayList<>();

        flaws.forEach(flaw -> possibleModifications.addAll(resolve(flaw)));

        return possibleModifications;
    }

    private List<Operator> resolve(Flaw flaw){
        if(flaw instanceof OpenCondition){
            return getAllResolvers((OpenCondition) flaw);
        }

        return getAllResolvers((Threat) flaw);

    }
    private List<Operator> getAllResolvers(OpenCondition openCondition) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(OpenConditionResolver.resolversByPromotion());
        resolvers.addAll(OpenConditionResolver.resolversByDemotion());
        resolvers.addAll(OpenConditionResolver.resolversByCreation());
        resolvers.addAll(OpenConditionResolver.resolversByCircumvention());

        return resolvers;
    }

    private List<Operator> getAllResolvers(Threat threat){
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(ThreatResolver.resolversByPromotion(threat));
        resolvers.addAll(ThreatResolver.resolversByDemotion(threat));
        resolvers.addAll(ThreatResolver.resolversByCircumvention(threat));

        return resolvers;
    }

    public boolean isExecutable() {
    }

    public boolean isCoherent() {
        return this.codenotationConstraints.isCoherent() && this.temporalConstraints.isCoherent();
    }

    public State applyPlanModification(PlanModification operator) {

    }
}
