package aStar_planning.pop_with_norms.components;

import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Organization {
    private Institution institution;
    private List<Norm> norms;
    private List<Predicate> assertions;
}