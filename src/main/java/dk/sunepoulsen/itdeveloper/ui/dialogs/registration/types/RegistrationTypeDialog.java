package dk.sunepoulsen.itdeveloper.ui.dialogs.registration.types;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.dialogs.DialogHelper;
import dk.sunepoulsen.itdeveloper.ui.dialogs.DialogImplementor;
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrationTypeDialog extends GridPane implements Initializable, DialogImplementor<RegistrationTypeModel> {
    private final RegistrationTypeModel model;

    @FXML
    private TextField nameField = null;

    @FXML
    private TextArea descriptionField = null;

    @FXML
    private TextArea purposeField = null;

    @FXML
    private CheckBox projectTimeField = null;

    private DialogHelper<RegistrationTypeModel> dialogHelper;

    public RegistrationTypeDialog() {
        this(new RegistrationTypeModel());
    }

    public RegistrationTypeDialog(RegistrationTypeModel model) {
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

    public Optional<RegistrationTypeModel> showAndWait() {
        return dialogHelper.showAndWait(nameField);
    }

    @Override
    public RegistrationTypeModel toModel() {
        RegistrationTypeModel result = new RegistrationTypeModel();
        result.setId(model.getId());
        result.setName(nameField.getText());
        result.setDescription(descriptionField.getText());
        result.setPurpose(purposeField.getText());
        result.setProjectTime(projectTimeField.isSelected());

        return result;
    }

    @Override
    public void toControls() {
        nameField.setText(model.getName());
        descriptionField.setText(model.getDescription());
        purposeField.setText(model.getPurpose());
        projectTimeField.setSelected(model.getProjectTime());
    }
}
