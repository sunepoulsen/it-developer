package dk.sunepoulsen.timelog.ui.dialogs.registration.types;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.dialogs.DialogHelper;
import dk.sunepoulsen.timelog.ui.dialogs.DialogImplementor;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrationTypeDialog extends GridPane implements Initializable, DialogImplementor<RegistrationTypeModel> {
    private RegistrationTypeModel model;

    @FXML
    private TextField nameField = null;

    @FXML
    private TextArea descriptionField = null;

    @FXML
    private TextArea purposeField = null;

    @FXML
    private CheckBox allDayField = null;

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
        dialogHelper = new DialogHelper<>();
        dialogHelper.initialize(this, resources);

        toControls();

        nameField.textProperty().addListener( ( observable, oldValue, newValue ) -> dialogHelper.disableButtons(toModel()) );
        nameField.focusedProperty().addListener( ( observable, oldValue, newValue ) -> dialogHelper.disableButtons(toModel()) );
    }

    public Optional<RegistrationTypeModel> showAndWait() {
        Platform.runLater( () -> nameField.requestFocus() );
        return dialogHelper.showAndWait();
    }

    @Override
    public RegistrationTypeModel convertControls(ButtonType buttonType) {
        if( !buttonType.equals( ButtonType.OK ) ) {
            return null;
        }

        return toModel();
    }

    private RegistrationTypeModel toModel() {
        RegistrationTypeModel result = new RegistrationTypeModel();
        result.setId(model.getId());
        result.setName(nameField.getText());
        result.setDescription(descriptionField.getText());
        result.setPurpose(purposeField.getText());
        result.setReasons(model.getReasons());
        result.setAllDay(allDayField.isSelected());

        return result;
    }

    private void toControls() {
        nameField.setText(model.getName());
        descriptionField.setText(model.getDescription());
        purposeField.setText(model.getPurpose());
        allDayField.setSelected(model.isAllDay());
    }
}
