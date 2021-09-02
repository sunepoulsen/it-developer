package dk.sunepoulsen.timelog.ui.topcomponents.registration.systems;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.dialogs.registration.systems.RegistrationSystemDialog;
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class RegistrationSystemsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<RegistrationSystemModel> viewer;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;


    public RegistrationSystemsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        backendConnection.getEvents().getRegistrationSystems().getCreatedEvent().addListener( v -> reload() );
        backendConnection.getEvents().getRegistrationSystems().getUpdatedEvent().addListener( v -> reload() );
        backendConnection.getEvents().getRegistrationSystems().getDeletedEvent().addListener( v -> reload() );

        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );

        reload();
    }

    private void updateButtonsState( ListChangeListener.Change<? extends RegistrationSystemModel> listener ) {
        ObservableList<? extends RegistrationSystemModel> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<RegistrationSystemModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> connection.servicesFactory().newRegistrationSystemsService().findAll()
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<RegistrationSystemModel> movies = task.getValue();

            log.info( "Viewing {} registration systems", movies.size() );
            viewer.setItems( movies );

            editButton.setDisable( true );
            deleteButton.setDisable( true );
        } );

        log.info( "Loading registration systems" );
        new Thread( task ).start();
    }

    @FXML
    private void viewerRowClicked( final MouseEvent mouseEvent ) {
        if( mouseEvent.getEventType().equals( MouseEvent.MOUSE_CLICKED ) &&
            mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2 )
        {
            showDialogAndUpdateRegistrationSystem();
        }
    }

    @FXML
    private void addButtonClicked( final ActionEvent event ) {
        showDialogAndCreateRegistrationSystem();
    }

    @FXML
    private void editButtonClicked( final ActionEvent event ) {
        showDialogAndUpdateRegistrationSystem();
    }

    @FXML
    private void deleteButtonClicked( final ActionEvent event ) {
        confirmAndDeleteRegistrationSystem();
    }

    @FXML
    private void showDialogAndCreateRegistrationSystem() {
        RegistrationSystemDialog dialog = new RegistrationSystemDialog();
        Optional<RegistrationSystemModel> model = dialog.showAndWait();

        model.ifPresent( registrationSystemModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newRegistrationSystemsService().create( registrationSystemModel )
            );
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateRegistrationSystem() {
        if( viewer.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        RegistrationSystemDialog dialog = new RegistrationSystemDialog( viewer.getSelectionModel().getSelectedItem() );
        Optional<RegistrationSystemModel> model = dialog.showAndWait();

        model.ifPresent( registrationSystemModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newRegistrationSystemsService().update( registrationSystemModel )
            );
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void confirmAndDeleteRegistrationSystem() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, bundle.getString( "alert.deletion.content.text" ) );
        alert.setHeaderText( bundle.getString( "alert.deletion.header.text" ) );
        alert.setTitle( bundle.getString( "alert.deletion.title.text" ) );

        alert.showAndWait()
            .filter( response -> response == ButtonType.OK )
            .ifPresent( response -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                    connection.servicesFactory().newRegistrationSystemsService().delete( viewer.getSelectionModel().getSelectedItems() )
                );
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
