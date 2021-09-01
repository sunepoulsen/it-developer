package dk.sunepoulsen.timelog.ui.model.registration.types;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationReasonModel {
    /**
     * Unique identifier of the registration reason.
     */
    private Long id;

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
