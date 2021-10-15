package dk.sunepoulsen.itdeveloper.ui.model.reporting;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeRegistrationReportingInterval {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeRegistrationReportingInterval(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
