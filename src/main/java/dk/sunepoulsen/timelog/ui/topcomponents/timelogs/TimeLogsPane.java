package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.control.cell.TreeTableValueFactory;
import dk.sunepoulsen.timelog.ui.dialogs.TimeLogDialog;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadWeekRegistrationsTask;
import dk.sunepoulsen.timelog.utils.AlertUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.time.LocalTime;
import java.util.ResourceBundle;

@Slf4j
public class TimeLogsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TimeLogsNavigationPane navigationPane;

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

    public TimeLogsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        navigationPane.getSelectedProperty().addListener((observable, oldValue, newValue) -> reload(newValue));

        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );

        typeNameTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::typeName));
        reasonTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::reason));
        startTimeTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::startTime));
        endTimeTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::endTime));
        workedHoursTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::workedHours));
        flexTableColumn.setCellValueFactory(new TreeTableValueFactory<>(TimeRegistration::flex));

        reload(navigationPane.getSelectedProperty().getValue());
    }

    private void updateButtonsState( ListChangeListener.Change<? extends TreeItem<TimeRegistration>> listener ) {
        ObservableList<? extends TreeItem<TimeRegistration>> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload(WeekModel weekModel) {
        StopWatch watch = new Log4JStopWatch();

        if (weekModel == null) {
            viewer.setRoot(new TreeItem<>());
            watch.stop("timelogs.load.none");
            return;
        }

        LoadWeekRegistrationsTask task = new LoadWeekRegistrationsTask( backendConnection, weekModel, registry.getLocale() );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<TimeRegistration> timeRegistrations = task.getValue();

            TreeItem<TimeRegistration> rootItem = new TreeItem<>();
            timeRegistrations.forEach(timeRegistration -> rootItem.getChildren().add(
                new TimeRegistrationTreeItem(timeRegistration)
            ));

            log.info( "Viewing {} time registrations", timeRegistrations.size() );
            viewer.setRoot( rootItem );

            editButton.setDisable( true );
            deleteButton.setDisable( true );

            watch.stop("timelogs.load.task");
        } );

        log.info( "Loading agreements" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
    }

    @FXML
    private void viewerRowClicked( final MouseEvent mouseEvent ) {
        if( mouseEvent.getEventType().equals( MouseEvent.MOUSE_CLICKED ) &&
            mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2 )
        {
            showDialogAndUpdateAgreement();
        }
    }

    @FXML
    private void addButtonClicked( final ActionEvent event ) {
        showDialogAndCreateAgreement();
    }

    @FXML
    private void editButtonClicked( final ActionEvent event ) {
        showDialogAndUpdateAgreement();
    }

    @FXML
    private void deleteButtonClicked( final ActionEvent event ) {
        confirmAndDeleteAgreement();
    }

    @FXML
    private void showDialogAndCreateAgreement() {
        new TimeLogDialog().showAndWait().ifPresent(timeLogModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newTimeLogsService().create( timeLogModel )
            );
            task.setOnSucceeded(event -> reload(navigationPane.getSelectedProperty().getValue()));
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateAgreement() {
        if( viewer.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        AlertUtils.notImplementedYet();
    }

    @FXML
    private void confirmAndDeleteAgreement() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, bundle.getString( "alert.deletion.content.text" ) );
        alert.setHeaderText( bundle.getString( "alert.deletion.header.text" ) );
        alert.setTitle( bundle.getString( "alert.deletion.title.text" ) );

        AlertUtils.notImplementedYet();
    }
}
