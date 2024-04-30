package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Interval {
    private PopSituation beginningSituation;
    private PopSituation endingSituation;

    public boolean hasNoEnd(){
        return this.endingSituation == null;
    }

    public boolean isInstant(){
        return this.beginningSituation == this.endingSituation;
    }
}
