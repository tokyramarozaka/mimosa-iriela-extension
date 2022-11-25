package time;

import aStar_planning.pop.PopSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Interval {
    private PopSituation startingSituation;
    private PopSituation endingSituation;

    public boolean overlaps(PopSituation popSituation) {
        return true;
    }

    public boolean intersects(Interval toTest){
        return true;
    }
}
