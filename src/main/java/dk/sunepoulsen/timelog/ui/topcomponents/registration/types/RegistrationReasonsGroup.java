package dk.sunepoulsen.timelog.ui.topcomponents.registration.types;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.dialogs.registration.types.RegistrationReasonDialog;
import dk.sunepoulsen.timelog.ui.dialogs.registration.types.RegistrationTypeDialog;
import dk.sunepoulsen.timelog.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.timelog.utils.AlertUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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

import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

@Slf4j
public class RegistrationReasonsGroup extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<RegistrationReasonModel> viewer;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button addButton = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    @Getter
    private SimpleObjectProperty<RegistrationTypeModel> currentRegistrationTypeProperty;

    public RegistrationReasonsGroup() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );
        this.currentRegistrationTypeProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );
        currentRegistrationTypeProperty.addListener( ( observable, oldValue, newValue ) -> {
            addButton.disableProperty().setValue(newValue == null);
            reload();
        });

        addButton.disableProperty().setValue(true);
        reload();
    }

    private void updateButtonsState( ListChangeListener.Change<? extends RegistrationReasonModel> listener ) {
        ObservableList<? extends RegistrationReasonModel> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<RegistrationReasonModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> {
                if (currentRegistrationTypeProperty.getValue() != null) {
                    return connection.servicesFactory().newRegistrationReasonsService(currentRegistrationTypeProperty.getValue().getId()).findAll();
                }

                return Collections.emptyList();
            }
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<RegistrationReasonModel> reasons = task.getValue();

            log.info( "Viewing {} registration reasons", reasons.size() );
            viewer.setItems( reasons );

            editButton.setDisable( true );
            deleteButton.setDisable( true );
        } );

        log.info( "Loading registration reasons" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
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
        new RegistrationReasonDialog(currentRegistrationTypeProperty.getValue().getId()).showAndWait().ifPresent(registrationReasonModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newRegistrationReasonsService(currentRegistrationTypeProperty.getValue().getId()).create( registrationReasonModel )
            );
            task.setOnSucceeded(event -> reload());
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateRegistrationSystem() {
        if( viewer.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        new RegistrationReasonDialog( viewer.getSelectionModel().getSelectedItem() ).showAndWait()
            .ifPresent( registrationReasonModel -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                    connection.servicesFactory().newRegistrationReasonsService(currentRegistrationTypeProperty.getValue().getId()).update( registrationReasonModel )
                );
                task.setOnSucceeded(event -> reload());
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
                    connection.servicesFactory().newRegistrationReasonsService(currentRegistrationTypeProperty.getValue().getId()).delete( viewer.getSelectionModel().getSelectedItems() )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
