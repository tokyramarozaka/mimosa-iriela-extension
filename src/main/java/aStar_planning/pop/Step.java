package aStar_planning.pop;

import logic.Action;
import logic.ActionPrecondition;
import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.platform.commons.util.Preconditions;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Step implements PartiallyOrderedElement{
    private LogicalInstance actionInstance;

    public ActionPrecondition getActionPreconditions(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getPreconditions();
    }
}
