package iriela.mock_components.organizations;

import aStar_planning.pop_with_norms.components.ConstitutiveNorm;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Organization;
import logic.Predicate;
import iriela.mock_components.constants.Agent;
import iriela.mock_components.constants.Zones;
import iriela.mock_components.institutions.Global;
import java.util.List;

public class GlobalOrg {
    public static Organization get(){
        return new Organization(
                Global.get(),
                GlobalOrg.norms(),
                GlobalOrg.assertions()
        );
    }

    public static List<Norm> norms(){
        return List.of(
                new ConstitutiveNorm(Agent.SELF, Global.movingObject),
                new ConstitutiveNorm(Zones.A1, Global.zone),
                new ConstitutiveNorm(Zones.A2, Global.zone),
                new ConstitutiveNorm(Zones.A3, Global.zone),
                new ConstitutiveNorm(Zones.A4, Global.zone),
                new ConstitutiveNorm(Zones.B1, Global.zone),
                new ConstitutiveNorm(Zones.B2, Global.zone),
                new ConstitutiveNorm(Zones.B3, Global.zone),
                new ConstitutiveNorm(Zones.B4, Global.zone),
                new ConstitutiveNorm(Zones.C1, Global.zone),
                new ConstitutiveNorm(Zones.C2, Global.zone),
                new ConstitutiveNorm(Zones.C3, Global.zone),
                new ConstitutiveNorm(Zones.C4, Global.zone),
                new ConstitutiveNorm(Zones.D1, Global.zone),
                new ConstitutiveNorm(Zones.D2, Global.zone),
                new ConstitutiveNorm(Zones.D3, Global.zone),
                new ConstitutiveNorm(Zones.D4, Global.zone)
        );
    }

    public static List<Predicate> assertions(){
        return List.of(
                Global.located(Agent.SELF, Zones.A1),
                Global.areAdjacents(Zones.A1, Zones.B1),
                Global.areAdjacents(Zones.B1, Zones.A1),
                Global.areAdjacents(Zones.B1, Zones.C1),
                Global.areAdjacents(Zones.C1, Zones.B1),
                Global.areAdjacents(Zones.C1, Zones.D1),
                Global.areAdjacents(Zones.D1, Zones.C1),

                Global.areAdjacents(Zones.A2, Zones.B2),
                Global.areAdjacents(Zones.B2, Zones.A2),
                Global.areAdjacents(Zones.B2, Zones.C2),
                Global.areAdjacents(Zones.C2, Zones.B2),
                Global.areAdjacents(Zones.C2, Zones.D2),
                Global.areAdjacents(Zones.D2, Zones.C2),

                Global.areAdjacents(Zones.A3, Zones.B3),
                Global.areAdjacents(Zones.B3, Zones.A3),
                Global.areAdjacents(Zones.B3, Zones.C3),
                Global.areAdjacents(Zones.C3, Zones.B3),
                Global.areAdjacents(Zones.C3, Zones.D3),
                Global.areAdjacents(Zones.D3, Zones.C3),

                Global.areAdjacents(Zones.A4, Zones.B4),
                Global.areAdjacents(Zones.B4, Zones.A4),
                Global.areAdjacents(Zones.B4, Zones.C4),
                Global.areAdjacents(Zones.C4, Zones.B4),
                Global.areAdjacents(Zones.C4, Zones.D4),
                Global.areAdjacents(Zones.D4, Zones.C4),

                Global.areAdjacents(Zones.A1, Zones.A2),
                Global.areAdjacents(Zones.A2, Zones.A1),
                Global.areAdjacents(Zones.A2, Zones.A3),
                Global.areAdjacents(Zones.A3, Zones.A2),
                Global.areAdjacents(Zones.A3, Zones.A4),
                Global.areAdjacents(Zones.A4, Zones.A3),

                Global.areAdjacents(Zones.B1, Zones.B2),
                Global.areAdjacents(Zones.B2, Zones.B1),
                Global.areAdjacents(Zones.B2, Zones.B3),
                Global.areAdjacents(Zones.B3, Zones.B2),
                Global.areAdjacents(Zones.B3, Zones.B4),
                Global.areAdjacents(Zones.B4, Zones.B3),

                Global.areAdjacents(Zones.C1, Zones.C2),
                Global.areAdjacents(Zones.C2, Zones.C1),
                Global.areAdjacents(Zones.C2, Zones.C3),
                Global.areAdjacents(Zones.C3, Zones.C2),
                Global.areAdjacents(Zones.C3, Zones.C4),
                Global.areAdjacents(Zones.C4, Zones.C3),

                Global.areAdjacents(Zones.D1, Zones.D2),
                Global.areAdjacents(Zones.D2, Zones.D1),
                Global.areAdjacents(Zones.D2, Zones.D3),
                Global.areAdjacents(Zones.D3, Zones.D2),
                Global.areAdjacents(Zones.D3, Zones.D4),
                Global.areAdjacents(Zones.D4, Zones.D3)
        );
    }
}
