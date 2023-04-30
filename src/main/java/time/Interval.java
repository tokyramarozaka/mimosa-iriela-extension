package time;

import aStar_planning.pop.components.Plan;
import aStar_planning.pop.components.PopSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Interval {
    private Plan plan;
    private PopSituation startingSituation;
    private PopSituation endingSituation;

    /**
     * Checks if a situation is between the starting and ending situation, including both the
     * starting and ending situation.
     * @param situation : the situation we want to check if it is contained within this interval
     * @return true if the given situation is contained with the current interval, and false
     * otherwise.
     */
    public boolean contains(PopSituation situation) {
        if(startingSituation.equals(situation) || endingSituation.equals(situation)){
            return true;
        }

        return plan.getTc().isBefore(startingSituation, situation) &&
                plan.getTc().isBefore(situation, endingSituation);
    }
}
