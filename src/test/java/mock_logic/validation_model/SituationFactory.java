package mock_logic.validation_model;

import logic.Predicate;
import logic.Situation;
import logic.mappers.SituationMapper;
import mock_logic.validation_model.constants.Zones;

import java.util.List;

public class SituationFactory {
    public static planning.Situation getInitialSituation(){
        List<Predicate> predicateList = ZonesConfiguration.topologyOne();
        predicateList.add(PredicateFactory.inZone(Zones.A1));

        return new planning.Situation(predicateList);
    }

    public static Situation mapInitialSituationToContext(){
        return SituationMapper.convertToInternal(getInitialSituation());
    }
}
