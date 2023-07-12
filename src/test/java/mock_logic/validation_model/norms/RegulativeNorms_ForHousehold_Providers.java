package mock_logic.validation_model.norms;

import aStar_planning.pop_with_norms.components.norms.DeonticOperator;
import aStar_planning.pop_with_norms.components.norms.NormConditions;
import aStar_planning.pop_with_norms.components.norms.NormConsequences;
import aStar_planning.pop_with_norms.components.norms.NormativeAction;
import aStar_planning.pop_with_norms.components.norms.RegulativeNorm;
import mock_logic.validation_model.ActionFactory;
import mock_logic.validation_model.AtomFactory;
import mock_logic.validation_model.Zones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegulativeNorms_ForHousehold_Providers {
    private static NormConditions obligation_dummy_inNormalZone_conditions() {
        return new NormConditions(List.of(
                AtomFactory.inZone(Zones.X),
                AtomFactory.isNormal(Zones.X)
        ));
    }

    private static NormConsequences obligation_dummy_inNormalZone_consequences() {
        return new NormativeAction(ActionFactory.dummyAction());
    }

    private static RegulativeNorm obligation_dummy_inNormalZone(){
        return new RegulativeNorm(
                "Dummy normative action",
                DeonticOperator.OBLIGATION,
                obligation_dummy_inNormalZone_conditions(),
                obligation_dummy_inNormalZone_consequences()
        );
    }
    private static NormConditions prohibition_cut_inProtectedZone_conditions() {
        return new NormConditions(List.of(
                AtomFactory.inZone(Zones.X),
                AtomFactory.isProtected(Zones.X)
        ));
    }

    private static NormConsequences prohibition_cut_inProtectedZone_consequences() {
        return new NormativeAction(ActionFactory.cut());
    }

    public static RegulativeNorm prohibition_cut_inProtectedZone() {
        return new RegulativeNorm(
                "Prohibited to cut in protected zone",
                DeonticOperator.PROHIBITION,
                prohibition_cut_inProtectedZone_conditions(),
                prohibition_cut_inProtectedZone_consequences()
        );
    }

    private static NormConditions prohibition_move_toSacredZone_conditions() {
        return new NormConditions(List.of(
                AtomFactory.inZone(Zones.X),
                AtomFactory.areNeighbors(Zones.X, Zones.Y),
                AtomFactory.isSacred(Zones.Y)
        ));
    }

    private static NormConsequences prohibition_move_toSacredZone_consequences() {
        return new NormativeAction(ActionFactory.move());
    }

    public static RegulativeNorm prohibition_move_toSacredZone() {
        return new RegulativeNorm(
                "Prohibited to move to a sacred zone",
                DeonticOperator.PROHIBITION,
                prohibition_move_toSacredZone_conditions(),
                prohibition_move_toSacredZone_consequences()
        );
    }

    private static NormConditions permission_hunt_ifLicense_conditions() {
        return new NormConditions(List.of(
                AtomFactory.inZone(Zones.X),
                AtomFactory.isProtected(Zones.X),
                AtomFactory.haveLicense()
        ));
    }

    private static NormConsequences permission_hunt_ifLicense_consequences() {
        return new NormativeAction(ActionFactory.fish());
    }

    public static RegulativeNorm permission_hunt_ifLicense() {
        return new RegulativeNorm(
                "Permitted to hunt under license",
                DeonticOperator.PERMISSION,
                permission_hunt_ifLicense_conditions(),
                permission_hunt_ifLicense_consequences()
        );
    }

    private static NormConditions permission_cut_ifLicense_conditions() {
        return new NormConditions(List.of(
                AtomFactory.inZone(Zones.X),
                AtomFactory.isProtected(Zones.X),
                AtomFactory.haveLicense()
        ));
    }

    private static NormConsequences permission_cut_ifLicense_consequences() {
        return new NormativeAction(ActionFactory.cut());
    }

    public static RegulativeNorm permission_cut_ifLicense() {
        return new RegulativeNorm(
                "Permitted to cut under license",
                DeonticOperator.PERMISSION,
                permission_cut_ifLicense_conditions(),
                permission_cut_ifLicense_consequences()
        );
    }

    public static List<RegulativeNorm> allRegulativeNorms() {
        return new ArrayList<>(Arrays.asList(
                prohibition_cut_inProtectedZone(),
                prohibition_move_toSacredZone(),
                permission_hunt_ifLicense(),
                permission_cut_ifLicense()
                //obligation_dummy_inNormalZone()
        ));
    }
}
