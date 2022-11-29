package mirana_IT.zoning;

import java.util.Map;
import java.util.TreeMap;

public class WorldFactory {
    /**
     * Returns a pre-defined map
     * TODO : provide link for good visualization
     * @return
     */
    public static World generate_4x4_world(){
        Map<Integer, Zone> zones = new TreeMap<>();

        zones.put(1,ZoneFactory.create(ZoneType.BLANK));
        zones.put(2,ZoneFactory.create(ZoneType.BLANK));
        zones.put(3,ZoneFactory.create(ZoneType.SACRED, ZoneType.FOREST));
        zones.put(4,ZoneFactory.create(ZoneType.PROTECTED, ZoneType.FOREST));

        zones.put(5,ZoneFactory.create(ZoneType.BLANK));
        zones.put(6,ZoneFactory.create(ZoneType.PROTECTED, ZoneType.BLANK));
        zones.put(7,ZoneFactory.create(ZoneType.BLANK));
        zones.put(8,ZoneFactory.create(ZoneType.RIVER));

        zones.put(8,ZoneFactory.create(ZoneType.BLANK));
        zones.put(9,ZoneFactory.create(ZoneType.FOREST, ZoneType.RIVER));
        zones.put(10,ZoneFactory.create(ZoneType.PROTECTED, ZoneType.FOREST));
        zones.put(11,ZoneFactory.create(ZoneType.RIVER));

        zones.put(12,ZoneFactory.create(ZoneType.PROTECTED, ZoneType.RIVER));
        zones.put(13,ZoneFactory.create(ZoneType.RIVER));
        zones.put(14,ZoneFactory.create(ZoneType.FOREST));
        zones.put(15,ZoneFactory.create(ZoneType.PROTECTED));

        return new World(4, zones);
    }
}
