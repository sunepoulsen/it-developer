package dk.sunepoulsen.itdeveloper.ui.model.timelogs;

import dk.sunepoulsen.itdeveloper.ui.model.AbstractModel;

import java.time.LocalTime;
import java.util.List;

public interface TimeRegistration extends AbstractModel {
    String typeName();
    String reason();
    LocalTime startTime();
    LocalTime endTime();
    Double workedHours();
    Double flex();

    List<TimeRegistration> children();
}
