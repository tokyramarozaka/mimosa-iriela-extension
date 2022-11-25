package logic.utils;

import logic.Codenotation;
import logic.CodenotationConstraints;
import logic.Context;
import logic.ContextualTerm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class ContextChange {
    private Map<Context, Context> changes;

    public boolean hasReplacement(Context oldContext){
        return changes.containsKey(oldContext);
    }

    public ContextualTerm getReplacement(ContextualTerm oldContextualTerm){
        Context contextReplacement = this.changes.get(oldContextualTerm.getContext());

        if(contextReplacement == null){
            return oldContextualTerm;
        }else{
            return new ContextualTerm(contextReplacement, oldContextualTerm.getTerm());
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
