package dk.sunepoulsen.timelog.ui.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
}
