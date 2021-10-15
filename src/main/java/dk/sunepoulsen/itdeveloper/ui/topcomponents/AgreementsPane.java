package dk.sunepoulsen.itdeveloper.ui.topcomponents;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.control.cell.DoubleTableCell;
import dk.sunepoulsen.itdeveloper.ui.control.cell.LocalDateTableCell;
import dk.sunepoulsen.itdeveloper.ui.dialogs.AgreementDialog;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ResourceBundle;

import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_ZERO_VALUE_CLASS_NAME;

@Slf4j
public class AgreementsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<AgreementModel> viewer;

    @FXML
    private TableColumn<AgreementModel, LocalDate> startDateColumn;

    @FXML
    private TableColumn<AgreementModel, LocalDate> endDateColumn;

    @FXML
    private TableColumn<AgreementModel, Double> mondayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> tuesdayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> wednesdayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> thursdayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> fridayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> saturdayColumn;

    @FXML
    private TableColumn<AgreementModel, Double> sundayColumn;

    @FXML
    private Region veil = null;

    @FXML
    private ProgressIndicator progressIndicator = null;

    @FXML
    private Button editButton = null;

    @FXML
    private Button deleteButton = null;

    public AgreementsPane() {
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

        startDateColumn.setCellFactory(param -> new LocalDateTableCell<>(this.registry.getSettings().getModel().getCalendar().shortDateFormatter()));
        endDateColumn.setCellFactory(param -> new LocalDateTableCell<>(this.registry.getSettings().getModel().getCalendar().shortDateFormatter()));

        mondayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        tuesdayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        wednesdayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        thursdayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        fridayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        saturdayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));
        sundayColumn.setCellFactory(param -> DoubleTableCell.of(this.registry.getLocale(), FLEX_ZERO_VALUE_CLASS_NAME));

        reload();
    }

    private void updateButtonsState( ListChangeListener.Change<? extends AgreementModel> listener ) {
        ObservableList<? extends AgreementModel> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
        LoadBackendServiceItemsTask<AgreementModel> task = new LoadBackendServiceItemsTask<>( backendConnection, AgreementModel.PERFORMANCE_LOAD_TAG,
            connection -> connection.servicesFactory().newAgreementsService().findAll()
        );

        progressIndicator.progressProperty().bind( task.progressProperty() );
        veil.visibleProperty().bind( task.runningProperty() );
        progressIndicator.visibleProperty().bind( task.runningProperty() );

        task.setOnSucceeded( event -> {
            ObservableList<AgreementModel> movies = task.getValue();

            log.info( "Viewing {} agreements", movies.size() );
            viewer.setItems( movies );

            editButton.setDisable( true );
            deleteButton.setDisable( true );
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
        new AgreementDialog().showAndWait().ifPresent(agreementModel -> {
            ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, AgreementModel.PERFORMANCE_SAVE_TAG, connection ->
                connection.servicesFactory().newAgreementsService().create( agreementModel )
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

        new AgreementDialog( viewer.getSelectionModel().getSelectedItem() ).showAndWait()
            .ifPresent( agreementModel -> {
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, AgreementModel.PERFORMANCE_SAVE_TAG, connection ->
                    connection.servicesFactory().newAgreementsService().update( agreementModel )
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
                ExecuteBackendServiceTask task = new ExecuteBackendServiceTask( backendConnection, AgreementModel.PERFORMANCE_DELETE_TAG, connection ->
                    connection.servicesFactory().newAgreementsService().delete( viewer.getSelectionModel().getSelectedItems() )
                );
                task.setOnSucceeded(event -> reload());
                registry.getUiRegistry().getTaskExecutorService().submit( task );
            } );
    }
}
