package dk.sunepoulsen.timelog.ui.dialogs;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.ProjectAccountModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProjectAccountDialog extends GridPane implements Initializable, DialogImplementor<ProjectAccountModel> {
    private final ProjectAccountModel model;

    @FXML
    private TextField accountNumberField = null;

    @FXML
    private TextField descriptionField = null;

    @FXML
    private TextArea purposeField = null;

    private DialogHelper<ProjectAccountModel> dialogHelper;

    public ProjectAccountDialog() {
        this(new ProjectAccountModel());
    }

    public ProjectAccountDialog(ProjectAccountModel model) {
        this.model = model;
        FXMLUtils.initFxml(Registry.getDefault(), this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogHelper = new DialogHelper<>(this);
        dialogHelper.initialize(resources);

        accountNumberField.textProperty().addListener(dialogHelper::disableButtons);
        accountNumberField.focusedProperty().addListener(dialogHelper::disableButtons);
        descriptionField.textProperty().addListener(dialogHelper::disableButtons);
        descriptionField.focusedProperty().addListener(dialogHelper::disableButtons);
        purposeField.textProperty().addListener(dialogHelper::disableButtons);
        purposeField.focusedProperty().addListener(dialogHelper::disableButtons);
    }

    public Optional<ProjectAccountModel> showAndWait() {
        return dialogHelper.showAndWait(accountNumberField);
    }

    @Override
    public ProjectAccountModel toModel() {
        ProjectAccountModel result = new ProjectAccountModel();
        result.setId(model.getId());
        result.setAccountNumber(accountNumberField.getText());
        result.setDescription(descriptionField.getText());
        result.setPurpose(purposeField.getText());

        return result;
    }

    @Override
    public void toControls() {
        accountNumberField.setText(model.getAccountNumber());
        descriptionField.setText(model.getDescription());
        purposeField.setText(model.getPurpose());
    }
}
