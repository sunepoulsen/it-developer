package dk.sunepoulsen.timelog.ui.dialogs;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.settings.model.SettingsModel;
import dk.sunepoulsen.timelog.ui.converters.RegistrationReasonConverter;
import dk.sunepoulsen.timelog.ui.converters.RegistrationTypeConverter;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.timelog.utils.ControlUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class TimeLogDialog extends GridPane implements Initializable, DialogImplementor<TimeLogModel> {
    private final SettingsModel settings;
    private final BackendConnection backendConnection;
    private final ExecutorService executorService;

    private final TimeLogModel model;

    @FXML
    private DatePicker dateField = null;

    @FXML
    private TextField startTimeField = null;

    @FXML
    private TextField endTimeField = null;

    @FXML
    private ComboBox<RegistrationTypeModel> registrationTypeField = null;

    @FXML
    private ComboBox<RegistrationReasonModel> registrationReasonField = null;

    private DialogHelper<TimeLogModel> dialogHelper;

    public TimeLogDialog() {
        this(new TimeLogModel());
    }

    public TimeLogDialog(TimeLogModel model) {
        this.settings = Registry.getDefault().getSettings().getModel();
        this.backendConnection = Registry.getDefault().getBackendConnection();
        this.executorService = Registry.getDefault().getUiRegistry().getTaskExecutorService();
        this.model = model;

        FXMLUtils.initFxml(Registry.getDefault(), this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogHelper = new DialogHelper<>(this);
        dialogHelper.initialize(resources);

        dateField.setConverter(settings.getCalendar().shortDateConverter());
        dateField.focusedProperty().addListener(dialogHelper::disableButtons);

        startTimeField.focusedProperty().addListener(dialogHelper::disableButtons);
        endTimeField.focusedProperty().addListener(dialogHelper::disableButtons);
        registrationTypeField.focusedProperty().addListener(dialogHelper::disableButtons);
        registrationReasonField.focusedProperty().addListener(dialogHelper::disableButtons);

        registrationTypeField.getSelectionModel().selectedItemProperty().addListener(this::reloadRegistrationReasons);
        initializeRegistrationTypes();

        dialogHelper.disableButtons();
    }

    private void initializeRegistrationTypes() {
        StopWatch watch = new Log4JStopWatch("registration.types.load");
        LoadBackendServiceItemsTask<RegistrationTypeModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> connection.servicesFactory().newRegistrationTypesService().findAll()
        );
        task.setOnSucceeded( event -> {
            registrationTypeField.setConverter(new RegistrationTypeConverter(task.getValue()));
            registrationTypeField.setItems(task.getValue());
            registrationTypeField.setValue(model.getRegistrationType());

            watch.stop();
        });
        this.executorService.submit(task);
    }

    private void reloadRegistrationReasons(ObservableValue<? extends RegistrationTypeModel> observable, RegistrationTypeModel oldValue, RegistrationTypeModel newValue) {
        StopWatch watch = new Log4JStopWatch("registration.reasons.load");

        LoadBackendServiceItemsTask<RegistrationReasonModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> connection.servicesFactory().newRegistrationReasonsService(registrationTypeField.getValue().getId()).findAll()
        );
        task.setOnSucceeded( event -> {
            registrationReasonField.setConverter(new RegistrationReasonConverter(task.getValue()));
            registrationReasonField.setItems(task.getValue());
            registrationReasonField.setValue(model.getRegistrationReason());

            watch.stop();
        });
        this.executorService.submit(task);
    }

    public Optional<TimeLogModel> showAndWait() {
        return dialogHelper.showAndWait(dateField);
    }

    @Override
    public TimeLogModel toModel() {
        TimeLogModel result = new TimeLogModel();
        result.setId(model.getId());
        result.setDate(dateField.getValue());
        result.setStartTime(ControlUtils.getLocalTime(startTimeField, settings.getCalendar().shortTimeFormatter()));
        result.setEndTime(ControlUtils.getLocalTime(endTimeField, settings.getCalendar().shortTimeFormatter()));
        result.setRegistrationType(registrationTypeField.getValue());
        result.setRegistrationReason(registrationReasonField.getValue());

        return result;
    }

    @Override
    public void toControls() {
        dateField.setValue(model.getDate());
        ControlUtils.setLocalTime(startTimeField, model.getStartTime(), settings.getCalendar().shortTimeFormatter());
        ControlUtils.setLocalTime(endTimeField, model.getEndTime(), settings.getCalendar().shortTimeFormatter());
    }
}
