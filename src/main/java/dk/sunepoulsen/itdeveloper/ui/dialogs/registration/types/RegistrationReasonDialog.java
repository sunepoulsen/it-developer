package dk.sunepoulsen.itdeveloper.ui.dialogs.registration.types;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.dialogs.DialogHelper;
import dk.sunepoulsen.itdeveloper.ui.dialogs.DialogImplementor;
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrationReasonDialog extends GridPane implements Initializable, DialogImplementor<RegistrationReasonModel> {
    private final RegistrationReasonModel model;

    @FXML
    private TextField nameField = null;

    @FXML
    private TextArea descriptionField = null;

    @FXML
    private TextArea purposeField = null;

    private DialogHelper<RegistrationReasonModel> dialogHelper;

    public RegistrationReasonDialog(Long registrationTypeId) {
        this(new RegistrationReasonModel());
        this.model.setRegistrationTypeId(registrationTypeId);
    }

    public RegistrationReasonDialog(RegistrationReasonModel model) {
        this.model = model;
        FXMLUtils.initFxml(Registry.getDefault(), this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogHelper = new DialogHelper<>(this);
        dialogHelper.initialize(resources);

        nameField.textProperty().addListener(dialogHelper::disableButtons);
        nameField.focusedProperty().addListener(dialogHelper::disableButtons);
    }

    public Optional<RegistrationReasonModel> showAndWait() {
        return dialogHelper.showAndWait(nameField);
    }

    @Override
    public RegistrationReasonModel toModel() {
        RegistrationReasonModel result = new RegistrationReasonModel();
        result.setId(model.getId());
        result.setRegistrationTypeId(model.getRegistrationTypeId());
        result.setName(nameField.getText());
        result.setDescription(descriptionField.getText());
        result.setPurpose(purposeField.getText());

        return result;
    }

    @Override
    public void toControls() {
        nameField.setText(model.getName());
        descriptionField.setText(model.getDescription());
        purposeField.setText(model.getPurpose());
    }
}
