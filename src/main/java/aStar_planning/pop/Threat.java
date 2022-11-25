package aStar_planning.pop;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Threat {
    private PopSituation situation;
    private ContextualAtom proposition;
    private Step destroyer;
}
