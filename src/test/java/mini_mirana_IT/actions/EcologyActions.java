package mini_mirana_IT.actions;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcologyActions {
    private static ActionPrecondition growResource_Preconditions(){
        return new ActionPrecondition();
    }

    private static ActionConsequence growResource_Consequences(){

    }

    public static Action growResource(){
        return new Action(
                 "Grow",
                growResource_Preconditions(),
                growResource_Consequences()
        );
    }
    public static List<Action> allActions(){
        return new ArrayList<>(Arrays.asList(
                growResource()
        ));
    }
}
