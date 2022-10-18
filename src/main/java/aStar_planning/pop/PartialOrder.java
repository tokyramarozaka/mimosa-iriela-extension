package aStar_planning.pop;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class PartialOrder {
    private PartiallyOrderedElement firstElement;
    private PartiallyOrderedElement secondElement;
}
