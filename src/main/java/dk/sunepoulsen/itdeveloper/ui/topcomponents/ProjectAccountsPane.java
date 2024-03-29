package dk.sunepoulsen.itdeveloper.ui.topcomponents;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.dialogs.ProjectAccountDialog;
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
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
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class ProjectAccountsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<ProjectAccountModel> viewer;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    public ProjectAccountsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );

        reload();
    }

    private void updateButtonsState( ListChangeListener.Change<? extends ProjectAccountModel> listener ) {
        ObservableList<? extends ProjectAccountModel> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<ProjectAccountModel> task = new LoadBackendServiceItemsTask<>( backendConnection, ProjectAccountModel.PERFORMANCE_LOAD_TAG,
            connection -> connection.servicesFactory().newProjectAccountsService().findAll()
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<ProjectAccountModel> movies = task.getValue();

            log.info( "Viewing {} project accounts", movies.size() );
            viewer.setItems( movies );

            editButton.setDisable( true );
            deleteButton.setDisable( true );
        } );

        log.info( "Loading project accounts" );
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
        new ProjectAccountDialog().showAndWait().ifPresent(projectAccountModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, ProjectAccountModel.PERFORMANCE_SAVE_TAG, connection ->
                connection.servicesFactory().newProjectAccountsService().create( projectAccountModel )
            );
            task.setOnSucceeded(event -> reload());
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateAgreement() {
        if( viewer.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        new ProjectAccountDialog( viewer.getSelectionModel().getSelectedItem() ).showAndWait()
            .ifPresent( projectAccountModel -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, ProjectAccountModel.PERFORMANCE_SAVE_TAG, connection ->
                    connection.servicesFactory().newProjectAccountsService().update( projectAccountModel )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }

    @FXML
    private void confirmAndDeleteAgreement() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, bundle.getString( "alert.deletion.content.text" ) );
        alert.setHeaderText( bundle.getString( "alert.deletion.header.text" ) );
        alert.setTitle( bundle.getString( "alert.deletion.title.text" ) );

        alert.showAndWait()
            .filter( response -> response == ButtonType.OK )
            .ifPresent( response -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, ProjectAccountModel.PERFORMANCE_DELETE_TAG, connection ->
                    connection.servicesFactory().newProjectAccountsService().delete( viewer.getSelectionModel().getSelectedItems() )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
