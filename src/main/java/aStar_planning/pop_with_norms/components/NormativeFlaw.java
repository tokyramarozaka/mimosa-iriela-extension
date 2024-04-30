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

@Getter
public class NormativeFlaw implements Flaw {
    private NormativePlan plan;
    private RegulativeNorm flawedNorm;
    private PopSituation applicableSituation;
    private Interval applicableInterval;
    private Context context;
    private Context intervalStartContext;

    private final static Logger logger = LogManager.getLogger(NormativeFlaw.class);

    public NormativeFlaw(
            NormativePlan plan,
            RegulativeNorm flawedNorm,
            PopSituation applicableSituation,
            Context context
    ) {
        this.plan = plan;
        this.flawedNorm = flawedNorm;
        this.applicableSituation = applicableSituation;
        this.context = context;
    }

    public NormativeFlaw(
            NormativePlan plan,
            RegulativeNorm flawedNorm,
            Interval applicableInterval,
            Context intervalStartContext
    ){
        this.plan = plan;
        this.flawedNorm = flawedNorm;
        this.applicableInterval = applicableInterval;
        this.intervalStartContext = intervalStartContext;
    }

    @Override
    public String toString() {
        if(this.context == null){
            return "(NORM): " +
                    this.flawedNorm +
                    " IN " + this.applicableInterval;
        }else{
            return "(NORM): " +
                    this.flawedNorm +
                    " IN " + this.applicableSituation
                    + "WITH CONTEXT " + this.context;
        }
    }
}
