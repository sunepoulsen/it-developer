package dk.sunepoulsen.timelog.ui.dialogs;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.AgreementModel;
import dk.sunepoulsen.timelog.utils.ControlUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AgreementDialog extends GridPane implements Initializable, DialogImplementor<AgreementModel> {
    private final AgreementModel model;

    @FXML
    private TextField nameField = null;

    @FXML
    private DatePicker startDateField = null;

    @FXML
    private DatePicker endDateField = null;

    @FXML
    private TextField mondayNormField = null;

    @FXML
    private TextField tuesdayNormField = null;

    @FXML
    private TextField wednesdayNormField = null;

    @FXML
    private TextField thursdayNormField = null;

    @FXML
    private TextField fridayNormField = null;

    @FXML
    private TextField saturdayNormField = null;

    @FXML
    private TextField sundayNormField = null;

    private DialogHelper<AgreementModel> dialogHelper;

    public AgreementDialog() {
        this(new AgreementModel());
    }

    public AgreementDialog(AgreementModel model) {
        this.model = model;
        FXMLUtils.initFxml(Registry.getDefault(), this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogHelper = new DialogHelper<>(this);
        dialogHelper.initialize(resources);

        nameField.textProperty().addListener(dialogHelper::disableButtons);
        startDateField.focusedProperty().addListener(dialogHelper::disableButtons);
        endDateField.focusedProperty().addListener(dialogHelper::disableButtons);
        mondayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        tuesdayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        wednesdayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        thursdayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        fridayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        saturdayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
        sundayNormField.focusedProperty().addListener(dialogHelper::disableButtons);
    }

    public Optional<AgreementModel> showAndWait() {
        return dialogHelper.showAndWait(nameField);
    }

    @Override
    public AgreementModel toModel() {
        AgreementModel result = new AgreementModel();
        result.setId(model.getId());
        result.setName(nameField.getText());
        result.setStartDate(startDateField.getValue());
        result.setEndDate(endDateField.getValue());
        result.setMondayNorm(ControlUtils.getDouble(mondayNormField));
        result.setTuesdayNorm(ControlUtils.getDouble(tuesdayNormField));
        result.setWednesdayNorm(ControlUtils.getDouble(wednesdayNormField));
        result.setThursdayNorm(ControlUtils.getDouble(thursdayNormField));
        result.setFridayNorm(ControlUtils.getDouble(fridayNormField));
        result.setSaturdayNorm(ControlUtils.getDouble(saturdayNormField));
        result.setSundayNorm(ControlUtils.getDouble(sundayNormField));

        return result;
    }

    @Override
    public void toControls() {
        nameField.setText(model.getName());
        startDateField.setValue(model.getStartDate());
        endDateField.setValue(model.getEndDate());
        ControlUtils.setDouble(mondayNormField, model.getMondayNorm());
        ControlUtils.setDouble(tuesdayNormField, model.getTuesdayNorm());
        ControlUtils.setDouble(wednesdayNormField, model.getWednesdayNorm());
        ControlUtils.setDouble(thursdayNormField, model.getThursdayNorm());
        ControlUtils.setDouble(fridayNormField, model.getFridayNorm());
        ControlUtils.setDouble(saturdayNormField, model.getSaturdayNorm());
        ControlUtils.setDouble(sundayNormField, model.getSundayNorm());
    }
}
