package dk.sunepoulsen.itdeveloper.ui.model.registration.types;

import dk.sunepoulsen.itdeveloper.ui.model.AbstractModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegistrationReasonModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "registration.reasons.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "registration.reasons.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "registration.reasons.delete.task";

    /**
     * Unique identifier of the registration reason.
     */
    private Long id;

    private Long registrationTypeId;

    @NotNull
    private String name;

    /**
     * Small description as a one liner. It's used in the table view.
     */
    private String description;

    /**
     * Purpose of this reason.
     */
    private String purpose;
}
