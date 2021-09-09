package dk.sunepoulsen.timelog.ui.model.timelogs;

import dk.sunepoulsen.timelog.ui.model.AbstractModel;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeLogModel implements AbstractModel {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long registrationTypeId;
    private Long registrationReasonId;
}
