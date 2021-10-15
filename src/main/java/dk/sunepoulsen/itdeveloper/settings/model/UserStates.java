package dk.sunepoulsen.itdeveloper.settings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by sunepoulsen on 15/06/2017.
 */
@Data
public class UserStates {
    @JsonProperty( "divider-positions" )
    private List<Double> dividerPositions;
}
