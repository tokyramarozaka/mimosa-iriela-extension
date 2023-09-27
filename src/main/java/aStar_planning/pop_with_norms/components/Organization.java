package aStar_planning.pop_with_norms.components;

import logic.Action;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import settings.Keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Organization {
    private Institution institution;
    private List<Norm> norms;
    private List<Predicate> assertions;

    public List<Action> allActionsForAgent() {
        List<Action> allActionsForAgent = new ArrayList<>();

        List<Role> agentRoles = this.getNorms()
                .stream()
                .filter(norm -> norm instanceof ConstitutiveNorm)
                .map(norm -> (ConstitutiveNorm) norm)
                .filter(constitutiveNorm -> constitutiveNorm.getSource().equals(Keywords.AGENT))
                .map(ConstitutiveNorm::getTarget)
                .map(constant -> (Role) constant)
                .toList();

        for (Role role : agentRoles) {
            this.getInstitution().getRoleActions()
                    .stream()
                    .filter(roleActions -> roleActions.getRole().equals(role))
                    .forEach(roleActions -> allActionsForAgent.addAll(roleActions.getActions()));
        }

        return allActionsForAgent;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getInstitution().getName()).append("Org");

        return stringBuilder.toString();
    }
}