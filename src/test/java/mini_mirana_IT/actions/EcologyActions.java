package mini_mirana_IT.actions;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import mini_mirana_IT.zoning.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcologyActions {
    private static ActionPrecondition growResource_Preconditions(World world){
        return new ActionPrecondition();
    }

    /**
     * TODO : the consequences of the growing action, basically updates every zone of the world
     * @param world
     * @return
     */
    private static ActionConsequence growResource_Consequences(World world){
        world.getZones().forEach((name, content) -> {

        });

        return null;
    }

    public static Action growResource(World world){
        return new Action(
                 "Grow",
                growResource_Preconditions(world),
                growResource_Consequences(world)
        );
    }
    public static List<Action> allActions(World world){
        return new ArrayList<>(Arrays.asList(
                growResource(world)
        ));
    }
}
