package dk.sunepoulsen.timelog.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude = {"selectedProperty"})
public class ProjectAccountModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "project.accounts.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "project.accounts.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "project.accounts.delete.task";

    protected BooleanProperty selectedProperty;

    public ProjectAccountModel() {
        this.selectedProperty = new SimpleBooleanProperty();
    }

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

    public BooleanProperty selectedProperty() {
        return this.selectedProperty;
    }
}
