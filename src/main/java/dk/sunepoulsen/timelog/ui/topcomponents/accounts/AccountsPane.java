package dk.sunepoulsen.timelog.ui.topcomponents.accounts;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.dialogs.accounts.AccountDialog;
import dk.sunepoulsen.timelog.ui.model.accounts.AccountModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.ExecuteBackendServiceTask;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
public class AccountsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<AccountModel> accountsView;

    @FXML
    private TableColumn<AccountModel, String> registrationSystemColumn;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    public AccountsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        registrationSystemColumn.setCellValueFactory( param -> {
            if( param != null && param.getValue() != null && param.getValue().getRegistrationSystem() != null ) {
                return new SimpleObjectProperty<>( param.getValue().getRegistrationSystem().getName() );
            }

            return new SimpleObjectProperty<>( "" );
        } );

        accountsView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        accountsView.getSelectionModel().getSelectedItems().addListener( this::updateButtonsListener );

        backendConnection.getEvents().getRegistrationSystems().getUpdatedEvent().addListener( v -> reload() );
        backendConnection.getEvents().getAccounts().getCreatedEvent().addListener( v -> reload() );
        backendConnection.getEvents().getAccounts().getUpdatedEvent().addListener( v -> reload() );
        backendConnection.getEvents().getAccounts().getDeletedEvent().addListener( v -> reload() );

        reload();
    }

    private void updateButtonsListener( ListChangeListener.Change<? extends AccountModel> listener ) {
        updateButtonsState( listener.getList() );
    }

    private void updateButtonsState( ObservableList<? extends AccountModel> list ) {
        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<AccountModel> task = new LoadBackendServiceItemsTask<>( backendConnection,
            connection -> connection.servicesFactory().newAccountsService().findAll()
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<AccountModel> movies = task.getValue();

            log.info( "Viewing {} accounts", movies.size() );
            accountsView.setItems( movies );
            updateButtonsState( FXCollections.emptyObservableList() );
        } );

        log.info( "Loading accounts" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
    }

    @FXML
    private void viewerRowClicked( final MouseEvent mouseEvent ) {
        if( mouseEvent.getEventType().equals( MouseEvent.MOUSE_CLICKED ) &&
            mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2 )
        {
            showDialogAndUpdateAccount();
        }
    }

    @FXML
    private void addButtonClicked( final ActionEvent event ) {
        showDialogAndCreateAccount();
    }

    @FXML
    private void editButtonClicked( final ActionEvent event ) {
        showDialogAndUpdateAccount();
    }

    @FXML
    private void deleteButtonClicked( final ActionEvent event ) {
        confirmAndDeleteAccounts();
    }

    private void showDialogAndCreateAccount() {
        AccountDialog dialog = new AccountDialog();
        Optional<AccountModel> model = dialog.showAndWait();

        model.ifPresent( accountModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newAccountsService().create( accountModel )
            );
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void showDialogAndUpdateAccount() {
        if( accountsView.getSelectionModel().getSelectedItem() == null ) {
            return;
        }

        AccountDialog dialog = new AccountDialog( accountsView.getSelectionModel().getSelectedItem() );
        Optional<AccountModel> model = dialog.showAndWait();

        model.ifPresent( accountModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                connection.servicesFactory().newAccountsService().update( accountModel )
            );
            registry.getUiRegistry().getTaskExecutorService().submit( task );
        } );
    }

    @FXML
    private void confirmAndDeleteAccounts() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, bundle.getString( "alert.deletion.content.text" ) );
        alert.setHeaderText( bundle.getString( "alert.deletion.header.text" ) );
        alert.setTitle( bundle.getString( "alert.deletion.title.text" ) );

        alert.showAndWait()
            .filter( response -> response == ButtonType.OK )
            .ifPresent( response -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, connection ->
                    connection.servicesFactory().newAccountsService().delete( accountsView.getSelectionModel().getSelectedItems() )
                );
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
