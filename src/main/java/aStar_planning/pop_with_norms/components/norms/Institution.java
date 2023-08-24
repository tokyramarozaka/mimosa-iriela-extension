package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Getter
public class Institution {
    private String name;
    private List<NormsPerRole> normsPerRoles;
    private List<Action> possibleActions;
    private float priority;

    public List<RegulativeNorm> getNormsForRole(Role role){
        try {
            return this.normsPerRoles.stream()
                    .filter(normsPerRole -> role.getName().equals(normsPerRole.getRole().getName()))
                    .findFirst()
                    .get()
                    .getNorms();
        }catch (NoSuchElementException e){
            return new ArrayList<>();
        }
    }

    public List<RegulativeNorm> getNormsForRoles(List<Role> roles){
        List<RegulativeNorm> normsForRoles = new ArrayList<>();

        roles.forEach(role -> {
            normsForRoles.addAll(getNormsForRole(role));
        });

        return normsForRoles;
    }

    @Override
    public String toString() {
        return this.name +
                "_INSTITUTION" +
                this.normsPerRoles +
                "\nRELATED ACTIONS :\n\t" +
                this.possibleActions;
    }
}