package dk.sunepoulsen.timelog.ui.topcomponents.registration.types;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.dialogs.registration.types.RegistrationTypeDialog;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadBackendServiceItemsTask;
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
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class RegistrationTypesGroup extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<RegistrationTypeModel> viewer;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    @Getter
    private SimpleObjectProperty<RegistrationTypeModel> selectedRegistrationTypeProperty;

    public RegistrationTypesGroup() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        selectedRegistrationTypeProperty = new SimpleObjectProperty<>();
        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateSelectedRegistrationType );

        reload();
    }

    private void updateSelectedRegistrationType(ListChangeListener.Change<? extends RegistrationTypeModel> listener) {
        ObservableList<? extends RegistrationTypeModel> list = listener.getList();
        if (list.size() != 1) {
            selectedRegistrationTypeProperty.setValue(null);
        }
        else {
            selectedRegistrationTypeProperty.setValue(list.get(0));
        }
    }

    private void updateButtonsState( ListChangeListener.Change<? extends RegistrationTypeModel> listener ) {
        ObservableList<? extends RegistrationTypeModel> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<RegistrationTypeModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> connection.servicesFactory().newRegistrationTypesService().findAll()
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<RegistrationTypeModel> movies = task.getValue();

            log.info( "Viewing {} registration types", movies.size() );
            viewer.setItems( movies );

            editButton.setDisable( true );
            deleteButton.setDisable( true );
        } );

        log.info( "Loading registration types" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
    }

    @FXML
    private void viewerRowClicked( final MouseEvent mouseEvent ) {
        if( mouseEvent.getEventType().equals( MouseEvent.MOUSE_CLICKED ) &&
            mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2 )
        {
            showDialogAndUpdateRegistrationType();
        }
    }

    @FXML
    private void addButtonClicked( final ActionEvent event ) {
        showDialogAndCreateRegistrationType();
    }

    @FXML
    private void editButtonClicked( final ActionEvent event ) {
        showDialogAndUpdateRegistrationType();
    }

    @FXML
    private void deleteButtonClicked( final ActionEvent event ) {
        confirmAndDeleteRegistrationType();
    }

    @FXML
    private void showDialogAndCreateRegistrationType() {
        new RegistrationTypeDialog().showAndWait().ifPresent(registrationTypeModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newRegistrationTypesService().create( registrationTypeModel )
            );
            task.setOnSucceeded(event -> reload());
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateRegistrationType() {
        if( viewer.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        new RegistrationTypeDialog( viewer.getSelectionModel().getSelectedItem() ).showAndWait()
            .ifPresent( registrationSystemModel -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                    connection.servicesFactory().newRegistrationTypesService().update( registrationSystemModel )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }

    @FXML
    private void confirmAndDeleteRegistrationType() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, bundle.getString( "alert.deletion.content.text" ) );
        alert.setHeaderText( bundle.getString( "alert.deletion.header.text" ) );
        alert.setTitle( bundle.getString( "alert.deletion.title.text" ) );

        alert.showAndWait()
            .filter( response -> response == ButtonType.OK )
            .ifPresent( response -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                    connection.servicesFactory().newRegistrationTypesService().delete( viewer.getSelectionModel().getSelectedItems() )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
