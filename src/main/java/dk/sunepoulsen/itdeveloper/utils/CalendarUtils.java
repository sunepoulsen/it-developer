package dk.sunepoulsen.itdeveloper.utils;

import java.time.LocalDate;
import java.util.Set;

public class CalendarUtils {
    /**
     * Returns the first date from a set that has the same week day as another date.
     */
    public static LocalDate findSameWeekDay(Set<LocalDate> dates, LocalDate weekDate) {
        return dates.stream()
            .filter(localDate -> localDate.getDayOfWeek().equals(weekDate.getDayOfWeek()))
            .findFirst()
            .orElse(null);
    }
}
