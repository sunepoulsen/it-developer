package dk.sunepoulsen.timelog.ui.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
public class ProjectAccountModel implements AbstractModel {
    /**
     * Unique identifier of the project account.
     */
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String accountNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    private String purpose;
}
