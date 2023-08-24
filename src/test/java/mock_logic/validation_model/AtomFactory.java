package mock_logic.validation_model;

import logic.Atom;
import logic.Term;

public class AtomFactory {
    public static Atom not(Atom atom){
        return new Atom(!atom.isNegation(), atom.getPredicate());
    }

    public static Atom haveFood(){
        return new Atom(false, PredicateFactory.haveFood());
    }

    public static Atom inZone(Term someZone){
        return new Atom(false, PredicateFactory.inZone(someZone));
    }

    public static Atom isProtected(Term someZone) {
        return new Atom(false, PredicateFactory.isProtected(someZone));
    }

    public static Atom haveWood() {
        return new Atom(false, PredicateFactory.haveWood());
    }

    public static Atom isForest(Term someZone) {
        return new Atom(false, PredicateFactory.isForest(someZone));
    }

    public static Atom haveLicense() {
        return new Atom(false, PredicateFactory.haveLicense());
    }

    public static Atom isSacred(Term someZone) {
        return new Atom(false, PredicateFactory.isSacred(someZone));
    }

    public static Atom isNormal(Term someZone) {
        return new Atom(false, PredicateFactory.isLand(someZone));
    }

    public static Atom areAdjacents(Term someZone, Term someOtherZone) {
        return new Atom(false, PredicateFactory.areAdjacents(someZone, someOtherZone));
    }

    public static Atom isOffice(Term someZone) {
        return new Atom(false, PredicateFactory.isOffice(someZone));
    }

    public static Atom haveMoney() {
        return new Atom(false, PredicateFactory.haveMoney());
    }

    public static Atom isRiver(Term someZone) {
        return new Atom(false, PredicateFactory.isRiver(someZone));
    }
}
