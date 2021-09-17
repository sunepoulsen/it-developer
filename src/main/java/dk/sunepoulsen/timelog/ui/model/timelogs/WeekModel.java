package dk.sunepoulsen.timelog.ui.model.timelogs;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;

public class WeekModel {
    /**
     * Date of the first day in the week
     */
    private final LocalDate firstDate;

    public WeekModel(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate firstDate() {
        return this.firstDate;
    }

    public LocalDate lastDate() {
        return this.firstDate.plusDays(6);
    }

    public Integer year() {
        return this.firstDate.getYear();
    }

    public Integer weekNumber() {
        return this.firstDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    public WeekModel nextWeek() {
        return new WeekModel(this.firstDate.plusWeeks(1));
    }

    public WeekModel previousWeek() {
        return new WeekModel(this.firstDate.minusWeeks(1));
    }

    public static WeekModel now() {
        return now(Clock.systemUTC());
    }

    public static WeekModel now(Clock clock) {
        LocalDate date = LocalDate.ofInstant(clock.instant(), clock.getZone());

        // Calculate monday of week
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return new WeekModel(date.minusDays(dayOfWeek.getValue() - 1));
    }

    public static WeekModel of(Integer weekNumber, Integer year) {
        LocalDate date = LocalDate.of(year, Month.FEBRUARY, 1);

        // Calculate monday of current week
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        date = date.minusDays(dayOfWeek.getValue() - 1);

        // Calculate date on monday of desired week
        Integer currentWeekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        if (weekNumber > currentWeekNumber ) {
            date = date.plusWeeks(weekNumber - currentWeekNumber);
        } else if (weekNumber < currentWeekNumber ) {
            date = date.minusWeeks(currentWeekNumber - weekNumber);
        }

        return new WeekModel(date);
    }
}
