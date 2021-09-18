package dk.sunepoulsen.timelog.ui.model.reporting;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeReportingInterval {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeReportingInterval(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
