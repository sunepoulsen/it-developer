package dk.sunepoulsen.itdeveloper.settings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by sunepoulsen on 15/06/2017.
 */
@Data
public class SettingsModel {
    @JsonProperty( "calendar" )
    private CalendarSettings calendar;
}
