package dk.sunepoulsen.itdeveloper.ui.model.timelogs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
public class DayRegistration implements TimeRegistration {
    private LocalDate date;
    private Double workingNorm;
    private Locale locale;
    private List<TimeRegistration> registrations;

    public DayRegistration(LocalDate date, Double workingNorm, Locale locale) {
        this.date = date;
        this.workingNorm = workingNorm;
        this.locale = locale;
        this.registrations = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {
    }

    @Override
    public String typeName() {
        return this.date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }

    @Override
    public String reason() {
        return null;
    }

    @Override
    public LocalTime startTime() {
        return registrations.stream()
            .map(TimeRegistration::startTime)
            .min(LocalTime::compareTo)
            .orElse(null);
    }

    @Override
    public LocalTime endTime() {
        return registrations.stream()
            .map(TimeRegistration::endTime)
            .max(LocalTime::compareTo)
            .orElse(null);
    }

    @Override
    public Double workedHours() {
        return registrations.stream().mapToDouble(TimeRegistration::workedHours).sum();
    }

    @Override
    public Double flex() {
        Double workedHours = workedHours();

        Double flex;
        if (workingNorm == null) {
            flex = workedHours;
        } else if (workedHours == null) {
            flex = -workingNorm;
        } else {
            flex = workedHours - workingNorm;
        }

        if (flex == 0.0) {
            flex = null;
        }

        return flex;
    }

    @Override
    public List<TimeRegistration> children() {
        return registrations;
    }

}
