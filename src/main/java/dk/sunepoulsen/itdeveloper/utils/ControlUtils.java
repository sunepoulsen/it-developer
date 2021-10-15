package dk.sunepoulsen.itdeveloper.utils;

import dk.sunepoulsen.itdeveloper.formatter.FlexFormatter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_NEGATIVE_VALUE_CLASS_NAME;
import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_POSITIVE_VALUE_CLASS_NAME;
import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_ZERO_VALUE_CLASS_NAME;

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

    public static void setFlexText(Label control, double value, FlexFormatter formatter) {
        control.setText(formatter.format(value));
        fillTextColor(control, value);
    }

    public static void fillTextColor(Label control, double value) {
        if (value > 0.0) {
            control.getStyleClass().add(FLEX_POSITIVE_VALUE_CLASS_NAME);
        } else if (value < 0.0) {
            control.getStyleClass().add(FLEX_NEGATIVE_VALUE_CLASS_NAME);
        }
        else {
            control.getStyleClass().add(FLEX_ZERO_VALUE_CLASS_NAME);
        }
    }

    public static void setLocalTime(TextField control, LocalTime localTime, DateTimeFormatter formatter) {
        if (localTime == null) {
            control.setText("");
            return;
        }

        control.setText(localTime.format(formatter));
    }

    public static LocalTime getLocalTime(TextField control, DateTimeFormatter formatter) {
        String text = null;

        try {
            text = getText(control);
            if (text.isEmpty() || text.isBlank()) {
                return null;
            }

            return LocalTime.parse(text, formatter);
        }
        catch(DateTimeParseException ex) {
            log.info("Unable to parse text '{}' as LocalTime. Using null value for LocalTime. Error: {}", text, ex.getMessage());
            log.debug("{}", ex.getMessage(), ex);
            return null;
        }
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
