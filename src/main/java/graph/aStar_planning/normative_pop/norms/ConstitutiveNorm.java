package graph.aStar_planning.normative_pop.norms;

import logic.Context;
import logic.LogicalEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConstitutiveNorm extends LogicalEntity implements Norm{
    private Role source;
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
}
