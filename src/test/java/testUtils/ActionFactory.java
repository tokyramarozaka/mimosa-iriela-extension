package testUtils;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;

import java.util.ArrayList;
import java.util.List;

public class ActionFactory {
    public static ActionPrecondition stackPreconditions(){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.holdBlock));
        preconditions.add(new Atom(false, PredicateFactory.freeBlock));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition dropPreconditions(){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.holdBlock));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition unstackPreconditions(){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.stackedBlocks));
        preconditions.add(new Atom(false, PredicateFactory.emptyArm));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition takePreconditions(){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.onTable));
        preconditions.add(new Atom(false, PredicateFactory.emptyArm));

        return new ActionPrecondition(preconditions);
    }
    public static List<Action> allActionsInBlocksWorld() {
        List<Action> possibleActions = new ArrayList<>();

        possibleActions.add(new Action("stack",new ActionPrecondition(),new ActionConsequence()));
        possibleActions.add(new Action("drop",new ActionPrecondition(),new ActionConsequence()));
        possibleActions.add(new Action("unstack", new ActionPrecondition(), new ActionConsequence()));
        possibleActions.add(new Action("take", new ActionPrecondition(), new ActionConsequence()));

        return possibleActions;
    }
}
