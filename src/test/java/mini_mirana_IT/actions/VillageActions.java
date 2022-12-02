package mini_mirana_IT.actions;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import norms.NormConditions;
import norms.NormConsequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VillageActions {
    private static ActionPrecondition registerPreconditions(){

    }

    private static ActionConsequence registerConsequences(){

    }

    public static Action register(){
        return new Action(
                "Register to village",
                registerPreconditions(),
                registerConsequences()
        );
    }

    private static ActionPrecondition unregisterPreconditions(){
        List<Atom> preconditions = new ArrayList<>();

        // TODO : specify the preconditions of the unregister action : registered()

        return new ActionPrecondition(preconditions);
    }

    private static ActionConsequence unregisterConsequence(){
        List<Atom> consequences = new ArrayList<>();

        // TODO : specify the consequences of the unregister action: !registered()

        return new ActionConsequence(consequences);
    }

    public static Action unregister(){
        return new Action(
                "unregister",
                unregisterPreconditions(),
                unregisterConsequence()
        );
    }

    public static List<Action> allActions(){
        return new ArrayList<>(Arrays.asList(
           register(),
           unregister()
        ));
    }
}
