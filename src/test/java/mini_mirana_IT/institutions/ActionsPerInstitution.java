package mini_mirana_IT.institutions;

import logic.Action;
import mini_mirana_IT.actions.EcologyActions;
import mini_mirana_IT.actions.HouseholdActions;
import mini_mirana_IT.actions.NorthernActions;
import mini_mirana_IT.actions.SouthernActions;
import mini_mirana_IT.actions.VillageActions;

import java.util.List;

/**
 * TODO : describe actions per institutions
 */
public class ActionsPerInstitution {
    public List<Action> household(){
        return HouseholdActions.allActions();
    }

    public List<Action> village(){
        return VillageActions.allActions();
    }

    public List<Action> ecology(){
        return EcologyActions.allActions();
    }

    public List<Action> northern(){
        return NorthernActions.allActions();
    }

    public List<Action> southern(){
        return SouthernActions.allActions();
    }
}
