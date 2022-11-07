package logic.utils;

import logic.Codenotation;
import logic.CodenotationConstraints;
import logic.Context;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
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

    public CodenotationConstraints apply(CodenotationConstraints toUpdate){
        List<Codenotation> resultingCodenotations = new ArrayList<>();

        toUpdate.getCodenotations().forEach(codenotation -> {
            resultingCodenotations.add(codenotation.updateContext(this));
        });

        return new CodenotationConstraints(resultingCodenotations);
    }

}
