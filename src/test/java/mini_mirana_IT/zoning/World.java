package mini_mirana_IT.zoning;

import graph.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class World {
    private Integer zonesPerLine = 4;
    private Map<Integer, Zone> zones;

    public World(){
        this.zones = new LinkedHashMap<>();
    }

    public Map<Position, Zone> initializeWorld(){
        Map<Position, Zone> world = new LinkedHashMap<>();

        world.putAll(Map.of(
            new Position(1,1), new Zone("Z1", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(1,2), new Zone("Z2", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(1,3), new Zone("Z3", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(1,4), new Zone("Z4", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(2,1), new Zone("Z5", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(2,2), new Zone("Z6", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(2,3), new Zone("Z7", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(2,4), new Zone("Z8", new ArrayList<>(Arrays.asList(ZoneType.BLANK)))
        ));

        world.putAll(Map.of(
            new Position(3,1), new Zone("Z9", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(3,2), new Zone("Z10", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(3,3), new Zone("Z11", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(3,4), new Zone("Z12", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(4,1), new Zone("Z13", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(4,2), new Zone("Z14", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(4,3), new Zone("Z15", new ArrayList<>(Arrays.asList(ZoneType.BLANK))),
            new Position(4,4), new Zone("Z16", new ArrayList<>(Arrays.asList(ZoneType.BLANK)))
        ));

        return world;
    }
}
