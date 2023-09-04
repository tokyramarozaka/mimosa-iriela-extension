package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.Concept;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Constant;
import logic.Context;
import logic.LogicalEntity;
import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class ConstitutiveNorm extends Norm{
    private final Predicate predicate;
    private final Constant concept;
    private final Constant role;

    public ConstitutiveNorm(Constant source, Constant target) {
        this.concept = source;
        this.role = target;
        this.predicate = new Predicate("is", List.of(this.concept, this.role));
    }


    @Override
    public LogicalEntity copy() {
        return new ConstitutiveNorm(this.concept, this.role);
    }


    @Override
    public String toString() {
        return concept + " COUNT AS " + role;
    }

    @Override
    public String getLabel() {
        return this.toString();
    }

    @Override
    public LogicalEntity build(Context context) {
        return null;
    }

    @Override
    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        return true;
    }
}
