package mock_logic.validation_model;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Term;

import java.util.ArrayList;
import java.util.List;

public class ActionFactory {
    public static ActionPrecondition getLicensePreconditions(Term someZone) {
        return new ActionPrecondition(List.of(
                AtomFactory.inZone(someZone),
                AtomFactory.isOffice(someZone)
        ));
    }

    public static ActionConsequence getLicenseConsequences() {
        return new ActionConsequence(List.of(
                AtomFactory.haveLicense()
        ));
    }

    public static Action getLicense() {
        return new Action(
                "getLicense",
                getLicensePreconditions(Zones.X),
                getLicenseConsequences()
        );
    }

    public static ActionPrecondition fishPreconditions(Term someZone) {
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(AtomFactory.inZone(someZone));
        preconditions.add(AtomFactory.isRiver(someZone));

        return new ActionPrecondition(preconditions);
    }

    public static ActionConsequence fishConsequences() {
        List<Atom> consequences = new ArrayList<>();

        consequences.add(new Atom(false, PredicateFactory.haveFood()));

        return new ActionConsequence(consequences);
    }

    public static Action fish() {
        return new Action("hunt", fishPreconditions(Zones.X), fishConsequences());
    }

    private static ActionPrecondition cutPreconditions(Term someZone) {
        return new ActionPrecondition(List.of(
                AtomFactory.isForest(someZone),
                AtomFactory.inZone(someZone)
        ));
    }

    private static ActionConsequence cutConsequences() {
        return new ActionConsequence(List.of(
                AtomFactory.haveWood()
        ));
    }


    public static Action cut() {
        return new Action("cut", cutPreconditions(Zones.X), cutConsequences());
    }

    private static ActionPrecondition movePreconditions(Term current, Term target) {
        return new ActionPrecondition(List.of(
                AtomFactory.inZone(current)
//                AtomFactory.areNeighbors(current, target)
        ));
    }

    private static ActionConsequence moveConsequences(Term current, Term target) {
        return new ActionConsequence(List.of(
                AtomFactory.inZone(target),
                AtomFactory.not(AtomFactory.inZone(current))
        ));
    }

    public static Action move() {
        return new Action(
                "move",
                movePreconditions(Zones.X,Zones.Y),
                moveConsequences(Zones.X,Zones.Y)
        );
    }

    public static Action dummyAction(){
        return new Action(
                "dummy action",
                new ActionPrecondition(),
                new ActionConsequence()
        );
    }
    public static List<Action> allActionsInExploitation() {
        return List.of(dummyAction(), cut(), fish(), getLicense(), move());
    }

    public static List<Action> allActionsInHousehold() {
        return List.of(
           dummyAction(), fish(), move(), cut()
        );
    }
}
