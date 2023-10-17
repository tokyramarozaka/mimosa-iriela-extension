package aStar_planning.pop_with_norms.components;

import aStar_planning.pop.components.PopSituation;
import constraints.CodenotationConstraints;
import logic.Constant;
import logic.Context;
import logic.LogicalEntity;
import lombok.Getter;

@Getter
public class ConstitutiveNorm extends Norm{
    private final Constant source;
    private final Constant target;

    public ConstitutiveNorm(Constant source, Constant target) {
        this.source = source;
        this.target = target;
    }


    @Override
    public LogicalEntity copy() {
        return new ConstitutiveNorm(this.source, this.target);
    }


    @Override
    public String toString() {
        return source + " COUNT AS " + target;
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
    public LogicalEntity build(Context context, CodenotationConstraints cc) {
        //TODO : represent all the constitutive norms using the CC as variable binding.
        return null;
    }

    @Override
    public boolean isApplied(NormativePlan plan, PopSituation situation,
                             CodenotationConstraints cc, Context applicableContext)
    {
        return true;
    }
}
