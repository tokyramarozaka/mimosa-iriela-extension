package mock_logic.validation_model;

import logic.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ZonesConfiguration {
    public static List<Predicate> configurationOne() {
        return new ArrayList<>(List.of(
                // Description of each zone
                // Row A : first top row of the map
                PredicateFactory.isLand(Zones.A1),
                PredicateFactory.isLand(Zones.A2),
                PredicateFactory.isForest(Zones.A3),
                PredicateFactory.isRiver(Zones.A4),

                // Row B : second top row of the map
                PredicateFactory.isLand(Zones.B1),
                PredicateFactory.isLand(Zones.B2),
                PredicateFactory.isLand(Zones.B3),
                PredicateFactory.isLand(Zones.B4),

                // Row C : third top row of the map
                PredicateFactory.isLand(Zones.C1),
                PredicateFactory.isLand(Zones.C2),
                PredicateFactory.isLand(Zones.C3),
                PredicateFactory.isOffice(Zones.C4),

                // Row D : fourth top row a.k.a. bottom row of the map
                PredicateFactory.isForest(Zones.D1),
                PredicateFactory.isRiver(Zones.D2),
                PredicateFactory.isRiver(Zones.D3),
                PredicateFactory.isForest(Zones.D4),

                // All adjacency
                // Horizontal adjacency - both orders needs to be stated to ensure symmetry
                PredicateFactory.areAdjacents(Zones.A1, Zones.A2),
                PredicateFactory.areAdjacents(Zones.A2, Zones.A1),
                PredicateFactory.areAdjacents(Zones.A2, Zones.A3),
                PredicateFactory.areAdjacents(Zones.A3, Zones.A2),
                PredicateFactory.areAdjacents(Zones.A3, Zones.A4),
                PredicateFactory.areAdjacents(Zones.A4, Zones.A3),

                PredicateFactory.areAdjacents(Zones.B1, Zones.B2),
                PredicateFactory.areAdjacents(Zones.B2, Zones.B1),
                PredicateFactory.areAdjacents(Zones.B2, Zones.B3),
                PredicateFactory.areAdjacents(Zones.B3, Zones.B2),
                PredicateFactory.areAdjacents(Zones.B3, Zones.B4),
                PredicateFactory.areAdjacents(Zones.B4, Zones.B3),

                PredicateFactory.areAdjacents(Zones.C1, Zones.C2),
                PredicateFactory.areAdjacents(Zones.C2, Zones.C1),
                PredicateFactory.areAdjacents(Zones.C2, Zones.C3),
                PredicateFactory.areAdjacents(Zones.C3, Zones.C2),
                PredicateFactory.areAdjacents(Zones.C3, Zones.C4),
                PredicateFactory.areAdjacents(Zones.C4, Zones.C3),

                PredicateFactory.areAdjacents(Zones.D1, Zones.D2),
                PredicateFactory.areAdjacents(Zones.D2, Zones.D1),
                PredicateFactory.areAdjacents(Zones.D2, Zones.D3),
                PredicateFactory.areAdjacents(Zones.D3, Zones.D2),
                PredicateFactory.areAdjacents(Zones.D3, Zones.D4),
                PredicateFactory.areAdjacents(Zones.D4, Zones.D3),

                // Vertical adjacency - both orders needs to be stated to ensure symmetry
                PredicateFactory.areAdjacents(Zones.A1, Zones.B1),
                PredicateFactory.areAdjacents(Zones.B1, Zones.A1),
                PredicateFactory.areAdjacents(Zones.B1, Zones.C1),
                PredicateFactory.areAdjacents(Zones.C1, Zones.B1),
                PredicateFactory.areAdjacents(Zones.C1, Zones.D1),
                PredicateFactory.areAdjacents(Zones.D1, Zones.C1),

                PredicateFactory.areAdjacents(Zones.A2, Zones.B2),
                PredicateFactory.areAdjacents(Zones.B2, Zones.A2),
                PredicateFactory.areAdjacents(Zones.B2, Zones.C2),
                PredicateFactory.areAdjacents(Zones.C2, Zones.B2),
                PredicateFactory.areAdjacents(Zones.C2, Zones.D2),
                PredicateFactory.areAdjacents(Zones.D2, Zones.C2),

                PredicateFactory.areAdjacents(Zones.A3, Zones.B3),
                PredicateFactory.areAdjacents(Zones.B3, Zones.A3),
                PredicateFactory.areAdjacents(Zones.B3, Zones.C3),
                PredicateFactory.areAdjacents(Zones.C3, Zones.B3),
                PredicateFactory.areAdjacents(Zones.C3, Zones.D3),
                PredicateFactory.areAdjacents(Zones.D3, Zones.C3),

                PredicateFactory.areAdjacents(Zones.A4, Zones.B4),
                PredicateFactory.areAdjacents(Zones.B4, Zones.A4),
                PredicateFactory.areAdjacents(Zones.B4, Zones.C4),
                PredicateFactory.areAdjacents(Zones.C4, Zones.B4),
                PredicateFactory.areAdjacents(Zones.C4, Zones.D4),
                PredicateFactory.areAdjacents(Zones.D4, Zones.C4)
        ));
    }
}
