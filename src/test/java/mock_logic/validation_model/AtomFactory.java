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

    public static Atom inForest(Term someForest) {
        return new Atom(false, PredicateFactory.inForest(someForest));
    }

    public static Atom inZone(Term someProtectedZone){
        return new Atom(false, PredicateFactory.inZone(someProtectedZone));
    }

    public static Atom isProtected(Term someZone) {
        return new Atom(false, PredicateFactory.isProtected(someZone));
    }

    public static Atom haveWood() {
        return new Atom(false, PredicateFactory.haveCoal());
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

    public static Atom areNeighbors(Term someZone, Term someOtherZone) {
        return new Atom(false, PredicateFactory.areNeighbors(someZone, someOtherZone));
    }

    public static Atom isOffice(Term someZone) {
        return new Atom(false, PredicateFactory.isOffice(someZone));
    }

    public static Atom haveMoney() {
        return new Atom(false, PredicateFactory.haveMoney());
    }
}