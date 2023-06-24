package mock_logic.validation_model;

import logic.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ZonesConfiguration {
    public static List<Predicate> configurationOne() {
        return new ArrayList<>(List.of(
                // Row A : first top row of the map
                PredicateFactory.isNormal(Zones.A1),

                PredicateFactory.isProtected(Zones.A2),
                PredicateFactory.isRiver(Zones.A2),

                PredicateFactory.isSacred(Zones.A3),
                PredicateFactory.isForest(Zones.A3),

                PredicateFactory.isNormal(Zones.A4),
                PredicateFactory.isForest(Zones.A4),

                // Row B : second top row of the map
                PredicateFactory.isRiver(Zones.B1),
                PredicateFactory.isSacred(Zones.B1),
                PredicateFactory.isNormal(Zones.B2),
                PredicateFactory.isNormal(Zones.B3),
                PredicateFactory.isNormal(Zones.B4),

                // Row C : third top row of the map
                PredicateFactory.isNormal(Zones.C1),
                PredicateFactory.isForest(Zones.C1),

                PredicateFactory.isNormal(Zones.C2),

                PredicateFactory.isNormal(Zones.C3),

                PredicateFactory.isNormal(Zones.C4),

                // Row D : fourth top row a.k.a. bottom row of the map
                PredicateFactory.isForest(Zones.D1),
                PredicateFactory.isSacred(Zones.D1),

                PredicateFactory.isRiver(Zones.D2),
                PredicateFactory.isProtected(Zones.D2),

                PredicateFactory.isNormal(Zones.D3),
                PredicateFactory.isRiver(Zones.D3),

                PredicateFactory.isForest(Zones.D4),
                PredicateFactory.isProtected(Zones.D4)
        ));
    }
}
