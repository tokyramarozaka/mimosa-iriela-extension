package validation_model_IT.zoning;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Zone {
    private String name;
    private List<ZoneType> zoneTypes;

    /**
     * TODO : find the String representation of the zone
     * @return
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
