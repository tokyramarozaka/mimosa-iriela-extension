package logic;

import aStar.Operator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class LogicalInstance implements Operator {
    private Context context;
    private LogicalEntity logicalEntity;

    private CodenotationConstraints codenotationConstraint;

    public LogicalInstance(LogicalEntity logicalEntity, Context context){
        this.logicalEntity = logicalEntity;
        this.context = context;
    }
}
