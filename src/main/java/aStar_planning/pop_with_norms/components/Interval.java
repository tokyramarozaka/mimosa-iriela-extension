package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class Interval {
    private PopSituation beginningSituation;
    private PopSituation endingSituation;

    public boolean hasNoEnd(){
        return this.endingSituation == null;
    }

    public boolean isInstant(){
        return this.beginningSituation == this.endingSituation;
    }

    public boolean isEmpty(){
        return this.beginningSituation == null && this.endingSituation == null;
    }

    @Override
    public String toString() {
        return "[" + this.beginningSituation + ","
                + this.endingSituation + "[";
    }
}
