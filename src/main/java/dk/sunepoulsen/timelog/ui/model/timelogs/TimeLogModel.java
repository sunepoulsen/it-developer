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
    public static final String PERFORMANCE_LOAD_TAG = "timelog.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "timelog.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "timelog.delete.task";

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
