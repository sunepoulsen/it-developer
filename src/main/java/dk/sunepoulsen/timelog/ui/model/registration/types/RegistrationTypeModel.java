package dk.sunepoulsen.timelog.ui.model.registration.types;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RegistrationTypeModel {
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
     * List of reasons that is associated with this registration type.
     */
    private List<RegistrationReasonModel> reasons;

    /**
     * Set if a registration of this type is an all day event.
     */
    private boolean allDay;
}
