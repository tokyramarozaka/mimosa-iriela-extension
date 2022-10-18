package aStar_planning.pop;

import logic.ContextualAtom;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class OpenCondition implements Flaw{
    private PopSituation situation;
    private ContextualAtom proposition;
}
