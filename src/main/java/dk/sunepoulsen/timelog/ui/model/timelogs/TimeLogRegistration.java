package dk.sunepoulsen.timelog.ui.model.timelogs;

import lombok.Data;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class TimeLogRegistration implements TimeRegistration {
    private TimeLogModel model;

    public TimeLogRegistration(TimeLogModel model) {
        this.model = model;
    }

    @Override
    public Long getId() {
        return model.getId();
    }

    @Override
    public void setId(Long id) {
        model.setId(id);
    }

    @Override
    public String typeName() {
        return model.getRegistrationType().getName();
    }

    @Override
    public String reason() {
        if (model.getRegistrationReason() != null) {
            return model.getRegistrationReason().getName();
        }

        return null;
    }

    @Override
    public LocalTime startTime() {
        return model.getStartTime();
    }

    @Override
    public LocalTime endTime() {
        return model.getEndTime();
    }

    @Override
    public Double workedHours() {
        if (model.getStartTime() != null && model.getEndTime() != null) {
            return ChronoUnit.MINUTES.between(model.getStartTime(), model.getEndTime()) / 60.0;
        }

        return null;
    }

    @Override
    public Double flex() {
        return null;
    }

    @Override
    public List<TimeRegistration> children() {
        return null;
    }
}
