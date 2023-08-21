package aStar_planning.pop_with_norms.components.norms;

import aStar_planning.pop.components.PopSituation;
import aStar_planning.pop_with_norms.Concept;
import aStar_planning.pop_with_norms.components.NormativePlan;
import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class ConstitutiveNorm extends Norm {
    private Concept source;
    private Role target;
    @Override
    public LogicalEntity build(Context context) {
        return null;
    }

    @Override
    public LogicalEntity copy() {
        return new ConstitutiveNorm(this.source, this.target);
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String toString() {
        return source + " COUNT AS " + target;
    }


    @Override
    public boolean isApplied(NormativePlan plan, PopSituation situation) {
        return true;
    }
}
