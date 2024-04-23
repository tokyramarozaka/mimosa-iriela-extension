package outputs.graphical_output;

import java.util.concurrent.atomic.AtomicInteger;

public class GraphvizDisplaySettings {
    public static int MAX_ALTERNATIVE_ARCS_PER_NODE = 1;
    public static AtomicInteger currentId = new AtomicInteger(0);
    public static int generateId() {
        return currentId.incrementAndGet();
    }
}
