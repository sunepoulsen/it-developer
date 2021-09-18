package dk.sunepoulsen.timelog.ui.model.reporting;

import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimeReporting {
    private RegistrationTypeModel registrationType;
    private Map<LocalDate, TimeReportingInterval> dates;

    public TimeReporting(RegistrationTypeModel registrationType) {
        this.registrationType = registrationType;
        this.dates = new HashMap<>();
    }
}
