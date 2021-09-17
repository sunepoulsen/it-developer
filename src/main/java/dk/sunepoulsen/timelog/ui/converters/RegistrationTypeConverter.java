package dk.sunepoulsen.timelog.ui.converters;

import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import javafx.util.StringConverter;

import java.util.List;

public class RegistrationTypeConverter extends StringConverter<RegistrationTypeModel> {
    private final List<RegistrationTypeModel> types;

    public RegistrationTypeConverter(List<RegistrationTypeModel> types) {
        this.types = types;
    }

    @Override
    public String toString(RegistrationTypeModel object) {
        if (object == null) {
            return null;
        }

        return object.getName();
    }

    @Override
    public RegistrationTypeModel fromString(String string) {
        return types.stream()
            .filter(registrationTypeModel -> registrationTypeModel.getName().equalsIgnoreCase(string))
            .findFirst()
            .orElse(null);
    }
}
