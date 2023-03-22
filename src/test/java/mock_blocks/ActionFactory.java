package mock_blocks;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.Variable;

import java.util.ArrayList;
import java.util.List;

public class ActionFactory {
    public static ActionPrecondition stackPreconditions(Variable block1, Variable block2){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.hold(block1)));
        preconditions.add(new Atom(false, PredicateFactory.free(block2)));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition dropPreconditions(Variable block){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.hold(block)));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition unstackPreconditions(Variable block1, Variable block2){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.on(block1, block2)));
        preconditions.add(new Atom(false, PredicateFactory.emptyArm));

        return new ActionPrecondition(preconditions);
    }

    public static ActionPrecondition takePreconditions(Variable block){
        List<Atom> preconditions = new ArrayList<>();

        preconditions.add(new Atom(false, PredicateFactory.onTable(block)));
        preconditions.add(new Atom(false, PredicateFactory.free(block)));
        preconditions.add(new Atom(false, PredicateFactory.emptyArm));

        return new ActionPrecondition(preconditions);
    }

    public static ActionConsequence stackConsequences(Variable block1, Variable block2){
        List<Atom> consequences = new ArrayList<>();

        consequences.add(new Atom(false, PredicateFactory.emptyArm));
        consequences.add(new Atom(false, PredicateFactory.on(block1, block2)));
        consequences.add(new Atom(false, PredicateFactory.free(block1)));

        consequences.add(new Atom(true,PredicateFactory.hold(block1)));
        consequences.add(new Atom(true,PredicateFactory.free(block2)));

        return new ActionConsequence(consequences);
    }

    public static ActionConsequence dropConsequences(Variable block){
        List<Atom> consequences = new ArrayList<>();

        consequences.add(new Atom(false, PredicateFactory.onTable(block)));
        consequences.add(new Atom(false, PredicateFactory.free(block)));
        consequences.add(new Atom(false, PredicateFactory.emptyArm));

        consequences.add(new Atom(true, PredicateFactory.hold(block)));

        return new ActionConsequence(consequences);
    }

    public static ActionConsequence unstackConsequences(Variable block1, Variable block2){
        List<Atom> consequences = new ArrayList<>();

        consequences.add(new Atom(false, PredicateFactory.hold(block1)));
        consequences.add(new Atom(false, PredicateFactory.free(block2)));

        consequences.add(new Atom(true, PredicateFactory.on(block1, block2)));
        consequences.add(new Atom(true, PredicateFactory.free(block1)));
        consequences.add(new Atom(true, PredicateFactory.emptyArm));

        return new ActionConsequence(consequences);
    }

    public static ActionConsequence takeConsequences(Variable block){
        List<Atom> consequences = new ArrayList<>();

        consequences.add(new Atom(false, PredicateFactory.hold(block)));

        consequences.add(new Atom(true, PredicateFactory.free(block)));
        consequences.add(new Atom(true, PredicateFactory.emptyArm));
        consequences.add(new Atom(true, PredicateFactory.onTable(block)));


        return new ActionConsequence(consequences);
    }

    public static List<Action> allActionsInBlocksWorld() {
        List<Action> possibleActions = new ArrayList<>();
        Variable block_X = BlockFactory.createVariable("X");
        Variable block_Y = BlockFactory.createVariable("Y");

        possibleActions.add(new Action(
                "stack",
                stackPreconditions(block_X,block_Y),
                stackConsequences(block_X,block_Y))
        );

        possibleActions.add(new Action(
                "drop",
                dropPreconditions(block_X),
                dropConsequences(block_X))
        );

        possibleActions.add(new Action(
                "unstack",
                unstackPreconditions(block_X, block_Y),
                unstackConsequences(block_X, block_Y))
        );

        possibleActions.add(new Action(
                "take",
                takePreconditions(block_X),
                takeConsequences(block_X))
        );

        return possibleActions;
    }
}
