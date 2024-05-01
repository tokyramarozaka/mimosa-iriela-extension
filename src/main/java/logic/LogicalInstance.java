package logic;

import aStar.Operator;
import constraints.CodenotationConstraints;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class LogicalInstance implements Operator {
    private Context context;
    private LogicalEntity logicalEntity;

    private CodenotationConstraints codenotationConstraint;

    public LogicalInstance(LogicalEntity logicalEntity, Context context){
        this.logicalEntity = logicalEntity;
        this.context = context;
    }


    public String toStringWithCodenotations(CodenotationConstraints cc){
        return this.getLogicalEntity().build(this.getContext(), cc).toString() + ":"
                + this.getContext().getId();
    }

    @Override
    public String getName() {
        return this.getLogicalEntity().getLabel();
    }

    @Override
    public String toGraphArc() {
        return this.toString();
    }
    @Override
    public String toString() {
        return this.getLogicalEntity().build(this.context).toString()
                + ":" + context.getId();
    }
}
