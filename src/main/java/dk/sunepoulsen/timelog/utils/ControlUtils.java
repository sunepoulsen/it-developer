package dk.sunepoulsen.timelog.utils;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by sunepoulsen on 14/06/2017.
 */
@Slf4j
public class ControlUtils {
    public static String getText( TextField field ) {
        if( field == null ) {
            return "";
        }

        String s = field.getText();
        if( s != null ) {
            return s;
        }

        return "";
    }

    public static String getText( DatePicker control ) {
        if( control == null ) {
            return "";
        }

        if( control.editorProperty() == null ) {
            return "";
        }

        return getText( control.editorProperty().getValue() );
    }

    public static Double getDouble( TextField control ) {
        if( control == null ) {
            return null;
        }

        if( control.getText() == null ) {
            return null;
        }

        if( control.getText().isBlank() ) {
            return null;
        }

        if( control.getText().isEmpty() ) {
            return null;
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        try {
            return formatter.parse(control.getText()).doubleValue();
        } catch (ParseException ex) {
            log.info("{} is not a valid Double with Locale {}. Using null value instead", control.getText(), Locale.getDefault().toString());
            return null;
        }
    }

    public static void setDouble( TextField control, Double value ) {
        if( control == null ) {
            return;
        }

        if(value == null) {
            control.setText("");
            return;
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        control.setText(formatter.format(value));
    }
}
