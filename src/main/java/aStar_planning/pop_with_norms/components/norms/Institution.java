package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop_with_norms.utils.NormsPerRole;
import com.sun.source.tree.ReturnTree;
import logic.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Institution {
    private String name;
    private List<NormsPerRole> normsPerRoles;
    private List<Action> possibleActions;

    public List<RegulativeNorm> getNormsForRole(Role role){
        try {
            return this.normsPerRoles.stream()
                    .filter(normsPerRole -> role.getName().equals(normsPerRole.getRole().getName()))
                    .findFirst()
                    .get()
                    .getRegulativeNorms();
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
}