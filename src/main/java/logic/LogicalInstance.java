package logic;

import aStar.Operator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getLogicalEntity().build(this.context));

        return stringBuilder.toString();
    }

    @Override
    public String getName() {
        return this.getLogicalEntity().getLabel();
    }
}
