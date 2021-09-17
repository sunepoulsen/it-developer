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
}
