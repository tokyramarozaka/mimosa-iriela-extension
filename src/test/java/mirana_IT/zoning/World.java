package mirana_IT.zoning;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class World {
    private Integer zonesPerLine = 4;
    private Map<Integer, Zone> zones;

}
