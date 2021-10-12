package dk.sunepoulsen.timelog.ui.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
public class AgreementModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "agreements.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "agreements.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "agreements.delete.task";

    /**
     * Unique identifier of the agreement.
     */
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @PositiveOrZero
    private Double mondayNorm;

    @PositiveOrZero
    private Double tuesdayNorm;

    @PositiveOrZero
    private Double wednesdayNorm;

    @PositiveOrZero
    private Double thursdayNorm;

    @PositiveOrZero
    private Double fridayNorm;

    @PositiveOrZero
    private Double saturdayNorm;

    @PositiveOrZero
    private Double sundayNorm;

    public int numberOfWorkingDays() {
        int result = 0;

        if (mondayNorm != null) {
            result += 1;
        }

        if (tuesdayNorm != null) {
            result += 1;
        }

        if (wednesdayNorm != null) {
            result += 1;
        }

        if (thursdayNorm != null) {
            result += 1;
        }

        if (fridayNorm != null) {
            result += 1;
        }

        if (saturdayNorm != null) {
            result += 1;
        }

        if (sundayNorm != null) {
            result += 1;
        }

        return result;
    }

    public double weeklyNorm() {
        double result = 0.0;

        if (mondayNorm != null) {
            result += mondayNorm;
        }

        if (tuesdayNorm != null) {
            result += tuesdayNorm;
        }

        if (wednesdayNorm != null) {
            result += wednesdayNorm;
        }

        if (thursdayNorm != null) {
            result += thursdayNorm;
        }

        if (fridayNorm != null) {
            result += fridayNorm;
        }

        if (saturdayNorm != null) {
            result += saturdayNorm;
        }

        if (sundayNorm != null) {
            result += sundayNorm;
        }

        return result;
    }

    public double weekDayNorm(DayOfWeek dayOfWeek) {
        if (DayOfWeek.MONDAY.equals(dayOfWeek) && mondayNorm != null) {
            return mondayNorm;
        }

        if (DayOfWeek.TUESDAY.equals(dayOfWeek) && tuesdayNorm != null) {
            return tuesdayNorm;
        }

        if (DayOfWeek.WEDNESDAY.equals(dayOfWeek) && wednesdayNorm != null) {
            return wednesdayNorm;
        }

        if (DayOfWeek.THURSDAY.equals(dayOfWeek) && thursdayNorm != null) {
            return thursdayNorm;
        }

        if (DayOfWeek.FRIDAY.equals(dayOfWeek) && fridayNorm != null) {
            return fridayNorm;
        }

        if (DayOfWeek.SATURDAY.equals(dayOfWeek) && saturdayNorm != null) {
            return saturdayNorm;
        }

        if (DayOfWeek.SUNDAY.equals(dayOfWeek) && sundayNorm != null) {
            return sundayNorm;
        }

        return 0.0;
    }
}
