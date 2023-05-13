package mock_logic.validation_model;

import logic.Predicate;
import logic.Situation;
import logic.mappers.SituationMapper;
import java.util.List;

public class SituationFactory {
    public static Situation startsAtA1_withZoneConfigurationOne(){
        List<Predicate> predicateList = ZonesConfiguration.configurationOne();

        predicateList.add(PredicateFactory.inZone(Zones.A1));

        planning.Situation externalSituation = new planning.Situation(predicateList);

        return SituationMapper.convertToInternal(externalSituation);
    }
}
