package dk.sunepoulsen.itdeveloper.ui.model.registration.types;

import dk.sunepoulsen.itdeveloper.ui.model.AbstractModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegistrationTypeModel implements AbstractModel {
    public static final String PERFORMANCE_LOAD_TAG = "registration.types.load.task";
    public static final String PERFORMANCE_SAVE_TAG = "registration.types.save.task";
    public static final String PERFORMANCE_DELETE_TAG = "registration.types.delete.task";

    /**
     * Unique identifier of the registration type.
     */
    private LongProperty idProperty;

    private StringProperty nameProperty;

    /**
     * Small description as a one liner. It's used in the table view.
     */
    private StringProperty descriptionProperty;

    /**
     * Purpose of this registration type.
     */
    private StringProperty purposeProperty;

    /**
     * Set if a registration of this type needs to be split into project accounts.
     */
    private BooleanProperty projectTimeProperty;

    public RegistrationTypeModel() {
        this.idProperty = new SimpleLongProperty();
        this.nameProperty = new SimpleStringProperty();
        this.descriptionProperty = new SimpleStringProperty();
        this.purposeProperty = new SimpleStringProperty();
        this.projectTimeProperty = new SimpleBooleanProperty();
    }

    public LongProperty idProperty() {
        return this.idProperty;
    }

    @Override
    public Long getId() {
        return idProperty.getValue();
    }

    @Override
    public void setId(Long id) {
        this.idProperty.setValue(id);
    }

    public StringProperty nameProperty() {
        return this.nameProperty;
    }

    @NotNull
    @NotBlank
    @NotEmpty
    public String getName() {
        return nameProperty.getValue();
    }

    public void setName(String name) {
        this.nameProperty.setValue(name);
    }

    public StringProperty descriptionProperty() {
        return this.descriptionProperty;
    }

    public String getDescription() {
        return descriptionProperty.getValue();
    }

    public void setDescription(String description) {
        this.descriptionProperty.setValue(description);
    }

    public StringProperty purposeProperty() {
        return this.purposeProperty;
    }

    public String getPurpose() {
        return purposeProperty.getValue();
    }

    public void setPurpose(String purpose) {
        this.purposeProperty.setValue(purpose);
    }

    public BooleanProperty projectTimeProperty() {
        return this.projectTimeProperty;
    }

    public Boolean getProjectTime() {
        return projectTimeProperty.getValue();
    }

    public void setProjectTime(Boolean projectTime) {
        this.projectTimeProperty.setValue(projectTime);
    }
}
