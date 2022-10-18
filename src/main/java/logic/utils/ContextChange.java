package logic.utils;

import logic.CodenotationConstraints;
import logic.Context;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@ToString
public class ContextChange {
    private Map<Context, Context> changes;

    public boolean hasReplacement(Context oldContext){
        return changes.containsKey(oldContext);
    }

    public Context getReplacement(Context oldContext){
        if(this.changes.get(oldContext) == null){
            return oldContext;
        }else{
            return this.changes.get(oldContext);
        }
    }

    public CodenotationConstraints replace(CodenotationConstraints toUpdate){

    }
}
