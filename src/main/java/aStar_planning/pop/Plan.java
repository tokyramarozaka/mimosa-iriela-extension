package aStar_planning.pop;

import aStar.Operator;
import aStar.State;
import logic.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Plan implements State {
    private List<PopSituation> situations;
    private List<Step> steps;
    private CodenotationConstraints codenotationConstraints;
    private TemporalConstraints temporalConstraints;
    private Set<Flaw> flaws;

    public List<Operator> possibleModifications() {
        List<Operator> possibleModifications = new ArrayList<>();

        flaws.forEach(flaw -> {
            possibleModifications.addAll(resolve(flaw));
        });

        return possibleModifications;
    }

    private List<Operator> resolve(Flaw flaw){
        if(flaw instanceof OpenCondition){
            return resolve((OpenCondition) flaw);
        }

        return resolve((Threat) flaw);

    }
    private List<Operator> resolve(OpenCondition openCondition) {
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(OpenConditionResolver.resolversByPromotion());
        resolvers.addAll(OpenConditionResolver.resolversByDemotion());
        resolvers.addAll(OpenConditionResolver.resolversByCreation());
        resolvers.addAll(OpenConditionResolver.resolversByCircumvention());

        return resolvers;
    }

    private List<Operator> resolve(Threat threat){
        List<Operator> resolvers = new ArrayList<>();

        resolvers.addAll(ThreatResolver.resolversByPromotion(threat));
        resolvers.addAll(ThreatResolver.resolversByDemotion(threat));
        resolvers.addAll(ThreatResolver.resolversByCircumvention(threat));

        return resolvers;
    }

    public boolean isExecutable() {
        return false;
    }

    public boolean isCoherent() {
        return this.codenotationConstraints.isCoherent() && this.temporalConstraints.isCoherent();
    }

    public State applyPlanModification(Modification operator) {

    }
}
