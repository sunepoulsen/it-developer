package dk.sunepoulsen.timelog.ui.model.timelogs;

import dk.sunepoulsen.timelog.ui.model.AbstractModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.validation.validators.ValidTimeInterval;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ValidTimeInterval(properties = {"startTime", "endTime"})
public class TimeLogModel implements AbstractModel {
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private RegistrationTypeModel registrationType;
    private RegistrationReasonModel registrationReason;
}
