package dk.sunepoulsen.timelog.ui.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProjectAccountModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "project.accounts.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "project.accounts.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "project.accounts.delete.task";

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
