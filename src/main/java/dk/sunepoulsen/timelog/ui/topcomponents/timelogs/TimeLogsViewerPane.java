package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.control.cell.DoubleTreeTableCell;
import dk.sunepoulsen.timelog.ui.control.cell.TreeTableValueFactory;
import dk.sunepoulsen.timelog.ui.dialogs.TimeLogDialog;
import dk.sunepoulsen.timelog.ui.model.timelogs.DayRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadWeekRegistrationsTask;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class TimeLogsViewerPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TreeTableView<TimeRegistration> viewer;

    @FXML
    private TreeTableColumn<TimeRegistration, String> typeNameTableColumn;

    @FXML
    private TreeTableColumn<TimeRegistration, String> reasonTableColumn;

    @FXML
    private TreeTableColumn<TimeRegistration, LocalTime> startTimeTableColumn;

    @FXML
    private TreeTableColumn<TimeRegistration, LocalTime> endTimeTableColumn;

    @FXML
    private TreeTableColumn<TimeRegistration, Double> workedHoursTableColumn;

    @FXML
    private TreeTableColumn<TimeRegistration, Double> flexTableColumn;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    @Getter
    private SimpleObjectProperty<WeekModel> currentWeekProperty;

    @Getter
    private SimpleObjectProperty<TimeRegistration> currentTimeRegistrationProperty;

    public TimeLogsViewerPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle(getClass());
        this.currentWeekProperty = new SimpleObjectProperty<>();
        this.currentTimeRegistrationProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info("Initializing {} custom control", getClass().getSimpleName());

        currentWeekProperty.addListener((observable, oldValue, newValue) -> reload(newValue));

        viewer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewer.getSelectionModel().getSelectedItems().addListener(this::updateButtonsState);
        viewer.getSelectionModel().getSelectedItems().addListener(this::updateCurrentTimeRegistration);

        typeNameTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::typeName));
        reasonTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::reason));
        startTimeTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::startTime));
        endTimeTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::endTime));
        workedHoursTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::workedHours));
        workedHoursTableColumn.setCellFactory(param -> {
            DoubleTreeTableCell<TimeRegistration> cell = new DoubleTreeTableCell<>(Registry.getDefault().getLocale());
            cell.setPositiveColor(Color.BLACK);

            return cell;
        });
        flexTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::flex));
        flexTableColumn.setCellFactory(param -> new DoubleTreeTableCell<>(Registry.getDefault().getLocale()));

        reload(currentWeekProperty.getValue());
    }

    private void updateCurrentTimeRegistration(ListChangeListener.Change<? extends TreeItem<TimeRegistration>> listener) {
        ObservableList<? extends TreeItem<TimeRegistration>> list = listener.getList();
        if (list.size() == 1) {
            currentTimeRegistrationProperty.setValue(list.get(0).getValue());
        }
        else {
            currentTimeRegistrationProperty.setValue(null);
        }
    }

    private void updateButtonsState(ListChangeListener.Change<? extends TreeItem<TimeRegistration>> listener) {
        ObservableList<? extends TreeItem<TimeRegistration>> list = listener.getList();

        editButton.disableProperty().setValue(list.size() != 1 || !(list.get(0).getValue() instanceof TimeLogRegistration));

        Optional<TimeRegistration> foundTimeLogRegistration = list.stream()
            .map(TreeItem::getValue)
            .filter(TimeLogRegistration.class::isInstance)
            .findFirst();

        deleteButton.disableProperty().setValue(foundTimeLogRegistration.isEmpty());
    }

    private void reload(WeekModel weekModel) {
        if (weekModel == null) {
            viewer.setRoot(new TreeItem<>());
            return;
        }

        LoadWeekRegistrationsTask task = new LoadWeekRegistrationsTask(backendConnection, weekModel, registry.getLocale());

        progressIndicator.progressProperty().bind(task.progressProperty());
        veil.visibleProperty().bind(task.runningProperty());
        progressIndicator.visibleProperty().bind(task.runningProperty());

        task.setOnSucceeded(event -> {
            ObservableList<TimeRegistration> timeRegistrations = task.getValue();

            TreeItem<TimeRegistration> rootItem = new TreeItem<>();
            timeRegistrations.forEach(timeRegistration -> {
                TimeRegistrationTreeItem treeItem = new TimeRegistrationTreeItem(timeRegistration);
                rootItem.getChildren().add(treeItem);
            });

            log.info("Viewing {} time registrations", timeRegistrations.size());
            viewer.setRoot(rootItem);

            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });

        log.info("Loading agreements");
        registry.getUiRegistry().getTaskExecutorService().submit(task);
    }

    @FXML
    private void viewerRowClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
            mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2)
        {
            if (viewer.getSelectionModel().getSelectedItem().getValue() instanceof TimeLogRegistration) {
                showDialogAndUpdateAgreement();
            }
        }
    }

    @FXML
    private void addButtonClicked(final ActionEvent event) {
        showDialogAndCreateAgreement();
    }

    @FXML
    private void editButtonClicked(final ActionEvent event) {
        showDialogAndUpdateAgreement();
    }

    @FXML
    private void deleteButtonClicked(final ActionEvent event) {
        confirmAndDeleteAgreement();
    }

    @FXML
    private void showDialogAndCreateAgreement() {
        new TimeLogDialog(createModelForCreateTimeLog()).showAndWait().ifPresent(timeLogModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask(backendConnection, TimeLogModel.PERFORMANCE_SAVE_TAG, connection ->
                connection.servicesFactory().newTimeLogsService().create(timeLogModel)
            );
            task.setOnSucceeded(event -> reload(currentWeekProperty.getValue()));
            registry.getUiRegistry().getTaskExecutorService().submit(task);
        });
    }

    @FXML
    private void showDialogAndUpdateAgreement() {
        if (viewer.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        if (!(viewer.getSelectionModel().getSelectedItem().getValue() instanceof TimeLogRegistration)) {
            return;
        }

        TimeLogModel model = ((TimeLogRegistration) viewer.getSelectionModel().getSelectedItem().getValue()).getModel();
        new TimeLogDialog(model).showAndWait().ifPresent(timeLogModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask(backendConnection, TimeLogModel.PERFORMANCE_SAVE_TAG, connection ->
                connection.servicesFactory().newTimeLogsService().update(timeLogModel)
            );
            task.setOnSucceeded(event -> reload(currentWeekProperty.getValue()));
            registry.getUiRegistry().getTaskExecutorService().submit(task);
        });
    }

    @FXML
    private void confirmAndDeleteAgreement() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("alert.deletion.content.text"));
        alert.setHeaderText(bundle.getString("alert.deletion.header.text"));
        alert.setTitle(bundle.getString("alert.deletion.title.text"));

        List<TimeLogModel> models = viewer.getSelectionModel().getSelectedItems().stream()
            .map(TreeItem::getValue)
            .filter(TimeLogRegistration.class::isInstance)
            .map(TimeLogRegistration.class::cast)
            .map(TimeLogRegistration::getModel)
            .collect(Collectors.toList());

        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask(backendConnection, TimeLogModel.PERFORMANCE_DELETE_TAG, connection ->
                    connection.servicesFactory().newTimeLogsService().delete(models)
                );
                task.setOnSucceeded(event -> reload(currentWeekProperty.getValue()));
                registry.getUiRegistry().getTaskExecutorService().submit(task);
            });
    }

    private TimeLogModel createModelForCreateTimeLog() {
        TimeLogModel model = new TimeLogModel();

        TreeItem<TimeRegistration> timeRegistration = viewer.getSelectionModel().getSelectedItem();
        if (timeRegistration == null) {
            model.setDate(LocalDate.now());
        }
        else if (timeRegistration.getValue() instanceof DayRegistration dayRegistration) {
            model.setDate(dayRegistration.getDate());
        } else if (timeRegistration.getValue() instanceof TimeLogRegistration timeLogRegistration) {
            model.setDate(timeLogRegistration.getModel().getDate());
            model.setRegistrationType(timeLogRegistration.getModel().getRegistrationType());
            model.setRegistrationReason(timeLogRegistration.getModel().getRegistrationReason());
        }
        model.setStartTime(LocalTime.now());

        return model;
    }
}
