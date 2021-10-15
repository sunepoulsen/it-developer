package dk.sunepoulsen.itdeveloper.ui.topcomponents.timelogs;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.control.SelectProjectAccountsPane;
import dk.sunepoulsen.itdeveloper.ui.control.WeekNavigationPane;
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class TimeLogsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;

    @FXML
    private WeekNavigationPane navigationPane;

    @FXML
    private TimeLogsViewerPane timeLogsPane;

    @FXML
    private SelectProjectAccountsPane selectProjectAccountsPane;

    @FXML
    private TimeLogsChartPane chartPane;

    public TimeLogsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();

        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @FXML
    public void initialize() {
        log.info("Initializing {} custom control", getClass().getSimpleName());

        timeLogsPane.getCurrentWeekProperty().bind(navigationPane.getSelectedProperty());
        timeLogsPane.getCurrentTimeRegistrationProperty().addListener(this::refreshProjectAccounts);

        selectProjectAccountsPane.getSelectedResultProperty().addListener(this::storeProjectAccounts);

        chartPane.getCurrentWeekProperty().bind(navigationPane.getSelectedProperty());
    }

    private void refreshProjectAccounts(ObservableValue<? extends TimeRegistration> observableValue, TimeRegistration oldValue, TimeRegistration newValue) {
        if (newValue instanceof TimeLogRegistration timeLogRegistration) {
            if (timeLogRegistration.getModel().getRegistrationType().getProjectTime()) {
                log.debug("Refreshing project accounts for timelog {}", timeLogRegistration.getId());
                selectProjectAccountsPane.reload(timeLogRegistration.getModel().getProjectAccounts());
                return;
            }
        }

        selectProjectAccountsPane.clearContent();
    }

    private void storeProjectAccounts(ListChangeListener.Change<? extends ProjectAccountModel> listener) {
        if (timeLogsPane.getCurrentTimeRegistrationProperty().getValue() instanceof TimeLogRegistration timeLogRegistration) {
            TimeLogModel model = timeLogRegistration.getModel();
            model.setProjectAccounts(new ArrayList<>(listener.getList()));

            log.info("Update timelog {} with project accounts: {}", timeLogRegistration.getId(), listener.getList().stream().map(ProjectAccountModel::getAccountNumber).collect(Collectors.toList()));
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask(backendConnection, TimeLogModel.PERFORMANCE_SAVE_TAG, connection ->
                connection.servicesFactory().newTimeLogsService().update(model)
            );
            registry.getUiRegistry().getTaskExecutorService().submit(task);
        }
    }
}
