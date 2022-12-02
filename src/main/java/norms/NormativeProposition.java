package norms;

import logic.Atom;
import logic.Context;
import logic.ContextualAtom;

public class NormativeProposition extends ContextualAtom implements NormConsequences{
    public NormativeProposition(Context context, Atom atom) {
        super(context, atom);
    }
}
