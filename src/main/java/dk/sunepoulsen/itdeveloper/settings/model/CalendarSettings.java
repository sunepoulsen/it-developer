package dk.sunepoulsen.itdeveloper.settings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import lombok.Data;

import java.time.format.DateTimeFormatter;

/**
 * Created by sunepoulsen on 15/06/2017.
 */
@Data
public class CalendarSettings {
    @JsonProperty( "short-date-format" )
    private String shortDateFormat;

    @JsonProperty( "short-time-format" )
    private String shortTimeFormat;

    public DateTimeFormatter shortDateFormatter() {
        return DateTimeFormatter.ofPattern(shortDateFormat);
    }

    public LocalDateStringConverter shortDateConverter() {
        return new LocalDateStringConverter(shortDateFormatter(), shortDateFormatter());
    }

    public DateTimeFormatter shortTimeFormatter() {
        return DateTimeFormatter.ofPattern(shortTimeFormat);
    }

    public LocalTimeStringConverter shortTimeConverter() {
        return new LocalTimeStringConverter(shortTimeFormatter(), shortTimeFormatter());
    }
}
