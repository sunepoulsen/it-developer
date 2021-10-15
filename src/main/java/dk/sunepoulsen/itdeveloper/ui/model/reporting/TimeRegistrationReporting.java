package dk.sunepoulsen.itdeveloper.ui.model.reporting;

import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimeRegistrationReporting {
    private RegistrationTypeModel registrationType;
    private Map<LocalDate, TimeRegistrationReportingInterval> dates;

    public TimeRegistrationReporting(RegistrationTypeModel registrationType) {
        this.registrationType = registrationType;
        this.dates = new HashMap<>();
    }
}
