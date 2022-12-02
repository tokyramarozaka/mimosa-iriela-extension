package mini_mirana_IT.zoning;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ZoneFactory {
    public static Zone create(ZoneType ...typeToCreate) {
        return new Zone(
                "Zone",
                Arrays.stream(typeToCreate).collect(Collectors.toList())
        );
    }
}
