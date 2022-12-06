package aStar_planning.pop;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Threat implements Flaw{
    private Step destroyer;
    private Step threatened;
    private PopSituation situation;
    private ContextualAtom proposition;
}
