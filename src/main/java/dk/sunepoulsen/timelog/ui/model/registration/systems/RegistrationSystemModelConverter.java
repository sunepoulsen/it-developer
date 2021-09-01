package dk.sunepoulsen.timelog.ui.model.registration.systems;

import javafx.util.StringConverter;

import java.util.List;

public class RegistrationSystemModelConverter extends StringConverter<RegistrationSystemModel> {
    private List<RegistrationSystemModel> models;

    public RegistrationSystemModelConverter( List<RegistrationSystemModel> models ) {
        this.models = models;
    }

    @Override
    public String toString( RegistrationSystemModel object ) {
        if (object == null) {
            return "";
        }

        return object.getName();
    }

    @Override
    public RegistrationSystemModel fromString( String s ) {
        return models.stream()
            .filter( model -> model.getName().equals( s ) )
            .findFirst()
            .orElse( null );
    }
}
