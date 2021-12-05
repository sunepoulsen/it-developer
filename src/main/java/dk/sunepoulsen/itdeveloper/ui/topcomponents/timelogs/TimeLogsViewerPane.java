package dk.sunepoulsen.itdeveloper.ui.topcomponents.timelogs;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.control.cell.DoubleTreeTableCell;
import dk.sunepoulsen.itdeveloper.ui.control.cell.TreeTableValueFactory;
import dk.sunepoulsen.itdeveloper.ui.dialogs.TimeLogDialog;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.DayRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.CalculateFlexBalanceTask;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadWeekRegistrationsTask;
import dk.sunepoulsen.itdeveloper.utils.ControlUtils;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import dk.sunepoulsen.itdeveloper.formatter.FlexFormatter;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
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
    private Label weekWorkedField = null;

    @FXML
    private Label weekNormField = null;

    @FXML
    private Label openingBalanceField = null;

    @FXML
    private Label flexHoursField = null;

    @FXML
    private Label ultimateBalanceField = null;

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

        currentWeekProperty.addListener((observable, oldValue, newValue) -> loadTimeLogs(newValue));

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
            cell.setPositiveValueStyleClass(cell.getZeroValueStyleClass());

            return cell;
        });
        flexTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::flex));
        flexTableColumn.setCellFactory(param -> new DoubleTreeTableCell<>(Registry.getDefault().getLocale()));

        loadTimeLogs(currentWeekProperty.getValue());
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

    private void loadTimeLogs(WeekModel weekModel) {
        if (weekModel == null) {
            viewer.setRoot(new TreeItem<>());
            return;
        }

        LoadWeekRegistrationsTask task = new LoadWeekRegistrationsTask(backendConnection, weekModel, registry.getLocale());

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

            loadFlexBalance(weekModel, timeRegistrations);
        });

        log.info("Loading agreements");
        registry.getUiRegistry().getTaskExecutorService().submit(task);
    }

    private void loadFlexBalance(WeekModel weekModel, List<TimeRegistration> timeRegistrations) {
        LoadBackendServiceItemsTask<AgreementModel> loadAgreementTask = new LoadBackendServiceItemsTask<>(backendConnection, AgreementModel.PERFORMANCE_LOAD_TAG,
            connection -> connection.servicesFactory().newAgreementsService().findByDate(LocalDate.now())
        );

        loadAgreementTask.setOnSucceeded(agreementEvent -> {
            CalculateFlexBalanceTask flexTask = new CalculateFlexBalanceTask(backendConnection, weekModel.firstDate().minusDays(1));

            flexTask.setOnSucceeded(flexEvent -> {
                loadAgreementTask.getValue().stream().findFirst().ifPresent(agreement -> {
                    // Update labels
                    double weekWorkedHours = sumTimeRegistrations(timeRegistrations, TimeRegistration::workedHours);
                    double weekNormHours = sumTimeRegistrations(timeRegistrations, timeRegistration -> ((DayRegistration)timeRegistration).getWorkingNorm());
                    double flexHours = weekWorkedHours - weekNormHours;

                    FlexFormatter weekFormatter = new FlexFormatter(agreement.weeklyNorm(), agreement.weeklyNorm() / agreement.numberOfWorkingDays());
                    weekFormatter.setFormatWeeks(false);
                    weekFormatter.setFormatDays(false);
                    ControlUtils.setFlexText(weekWorkedField, weekWorkedHours, weekFormatter);
                    ControlUtils.setFlexText(weekNormField, weekNormHours, weekFormatter);

                    FlexFormatter balanceFormatter = new FlexFormatter(agreement.weeklyNorm(), agreement.weeklyNorm() / agreement.numberOfWorkingDays());
                    ControlUtils.setFlexText(openingBalanceField, flexTask.getValue(), balanceFormatter);
                    ControlUtils.setFlexText(flexHoursField, flexHours, balanceFormatter);
                    ControlUtils.setFlexText(ultimateBalanceField,flexTask.getValue() + flexHours, balanceFormatter);
                });
            });

            registry.getUiRegistry().getTaskExecutorService().submit(flexTask);
        });

        registry.getUiRegistry().getTaskExecutorService().submit(loadAgreementTask);
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
            task.setOnSucceeded(event -> loadTimeLogs(currentWeekProperty.getValue()));
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
            task.setOnSucceeded(event -> loadTimeLogs(currentWeekProperty.getValue()));
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
                task.setOnSucceeded(event -> loadTimeLogs(currentWeekProperty.getValue()));
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

    private Double sumTimeRegistrations(List<TimeRegistration> timeRegistrations, Function<TimeRegistration, Double> valueExtractor) {
        return timeRegistrations.stream()
            .filter(timeRegistration -> timeRegistration instanceof DayRegistration)
            .filter(timeRegistration -> !((DayRegistration)timeRegistration).getDate().isAfter(LocalDate.now()))
            .map(valueExtractor)
            .filter(Objects::nonNull)
            .mapToDouble(value -> value)
            .sum();
    }
}
