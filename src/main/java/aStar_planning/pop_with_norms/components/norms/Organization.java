package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop_with_norms.concepts.Concept;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Organization {
    private Institution institution;
    private List<Norm> norms;
    private List<Predicate> assertions;
}