package dk.sunepoulsen.timelog.ui.model.registration.types;

import dk.sunepoulsen.timelog.ui.model.AbstractModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationTypeModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "registration.types.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "registration.types.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "registration.types.delete.task";

    /**
     * Unique identifier of the registration type.
     */
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    /**
     * Small description as a one liner. It's used in the table view.
     */
    private String description;

    /**
     * Purpose of this registration type.
     */
    private String purpose;

    /**
     * Set if a registration of this type is an all day event.
     */
    private boolean allDay;
}
