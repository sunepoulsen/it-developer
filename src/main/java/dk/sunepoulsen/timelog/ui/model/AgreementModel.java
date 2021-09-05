package dk.sunepoulsen.timelog.ui.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Data
public class AgreementModel {
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
}
