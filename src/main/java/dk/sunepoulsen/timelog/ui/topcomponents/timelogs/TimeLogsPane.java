package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
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
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class TimeLogsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TreeTableView<TimeLogModel> viewer;

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

        viewer.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        viewer.getSelectionModel().getSelectedItems().addListener( this::updateButtonsState );

        reload();
    }

    private void updateButtonsState( ListChangeListener.Change<? extends TreeItem<TimeLogModel>> listener ) {
        ObservableList<? extends TreeItem<TimeLogModel>> list = listener.getList();

        editButton.disableProperty().setValue( list.size() != 1 );
        deleteButton.disableProperty().setValue( list.isEmpty() );
    }

    private void reload() {
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
        AlertUtils.notImplementedYet();
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
