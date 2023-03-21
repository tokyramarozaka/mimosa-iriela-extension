package mock_blocks;

import logic.Atom;
import logic.Constant;
import logic.Context;
import logic.ContextualAtom;
import logic.Predicate;

public class ContextualAtomFactory {
    public static Context context = new Context();
    public static Constant aBlock = BlockFactory.createConstant("A");

    public static ContextualAtom someProposition(){
        Atom proposition = new Atom(false, PredicateFactory.onTable(aBlock));
        return new ContextualAtom(context, proposition);
    }
}
