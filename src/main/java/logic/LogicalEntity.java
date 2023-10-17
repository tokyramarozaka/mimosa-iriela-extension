package logic;

import constraints.CodenotationConstraints;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class LogicalEntity {
    public LogicalInstance createInstance(){
        return new LogicalInstance(this, new Context());
    }

    public abstract LogicalEntity build(Context context);
    public abstract LogicalEntity build(Context context, CodenotationConstraints cc);

    public abstract LogicalEntity copy();

    public abstract String getLabel();
}
