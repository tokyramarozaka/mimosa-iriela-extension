package logic;

public abstract class LogicalEntity {
    public LogicalInstance createInstance(){
        return new LogicalInstance(this, new Context());
    }

    public abstract LogicalEntity build(Context context);

    public abstract LogicalEntity copy();
}
