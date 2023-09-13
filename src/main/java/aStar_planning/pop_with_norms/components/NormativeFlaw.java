package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop.components.Flaw;
import exception.UnapplicableNormException;
import logic.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@AllArgsConstructor
@Getter
public class NormativeFlaw implements Flaw {
    private NormativePlan plan;
    private RegulativeNorm flawedNorm;
    private PopSituation applicableSituation;
    private Context context;
    private final static Logger logger = LogManager.getLogger(NormativeFlaw.class);

    @Override
    public String toString() {
        String flawName = switch(flawedNorm.getDeonticOperator()){
            case OBLIGATION -> "\n\tMISSING OBLIGATION";
            case PROHIBITION -> "\n\tMISSING PROHIBITION";
            case PERMISSION -> "\n\tMISSING PERMISSION";
        };

        return flawName + " : " +
                this.flawedNorm +
                " IN " + this.applicableSituation
                + "WITH CONTEXT " + this.context;
    }

    /**
     * Get the non applicable situation in which the norm stops being applicable
     * @param applicableSituation
     * @return
     */
    public PopSituation getInapplicableSituationAfter(PopSituation applicableSituation) {
        List<PopSituation> situationsAfterApplication = plan.getSituations().stream()
                .filter(situation -> plan.getTc().isBefore(applicableSituation, situation))
                .toList();

        for (PopSituation situation : situationsAfterApplication) {
            try {
                this.flawedNorm.getApplicableCodenotations(plan, situation);
            } catch(UnapplicableNormException e){
                logger.info("Unapplicable exception found for : " + flawedNorm + " : " + situation);
                return situation;
            }
        }

        logger.info(" did not found unapplicable situation for norm : " + flawedNorm);
        return null;
    }
}
