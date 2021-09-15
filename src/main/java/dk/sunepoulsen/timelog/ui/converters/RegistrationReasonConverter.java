package dk.sunepoulsen.timelog.ui.converters;

import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationReasonModel;
import javafx.util.StringConverter;

import java.util.List;

public class RegistrationReasonConverter extends StringConverter<RegistrationReasonModel> {
    private final List<RegistrationReasonModel> reasons;

    public RegistrationReasonConverter(List<RegistrationReasonModel> reasons) {
        this.reasons = reasons;
    }

    @Override
    public String toString(RegistrationReasonModel object) {
        if (object == null) {
            return null;
        }

        return object.getName();
    }

    @Override
    public RegistrationReasonModel fromString(String string) {
        return reasons.stream()
            .filter(registrationTypeModel -> registrationTypeModel.getName().equalsIgnoreCase(string))
            .findFirst()
            .orElse(null);
    }
}
