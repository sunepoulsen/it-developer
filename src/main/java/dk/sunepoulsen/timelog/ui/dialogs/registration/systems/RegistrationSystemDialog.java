package dk.sunepoulsen.timelog.ui.dialogs.registration.systems;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModel;
import dk.sunepoulsen.timelog.utils.ControlUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by sunepoulsen on 11/06/2017.
 */
public class RegistrationSystemDialog extends GridPane implements Initializable {
    private RegistrationSystemModel model;

    @FXML
    private TextField nameField = null;

    @FXML
    private TextArea descriptionField = null;

    private Dialog<RegistrationSystemModel> dialog = null;

    private Registry registry;
    private ResourceBundle bundle;

    public RegistrationSystemDialog() {
        this( new RegistrationSystemModel() );
    }

    public RegistrationSystemDialog( RegistrationSystemModel model ) {
        this.registry = Registry.getDefault();
        this.model = model;

        FXMLUtils.initFxml(this.registry, this);
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        bundle = resources;

        dialog = new Dialog<>();
        dialog.setTitle( bundle.getString( "dialog.title.text" ) );
        dialog.setHeaderText( bundle.getString( "dialog.header.text" ) );

        dialog.getDialogPane().getButtonTypes().addAll( ButtonType.OK, ButtonType.CANCEL );
        dialog.getDialogPane().setContent( this );
        dialog.setResultConverter( this::convertControls );

        Node okButton = dialog.getDialogPane().lookupButton( ButtonType.OK );
        if( okButton != null ) {
            okButton.setDisable( true );
        }

        nameField.setText( model.getName() );
        nameField.textProperty().addListener( ( observable, oldValue, newValue ) -> disableButtons() );

        descriptionField.setText( model.getDescription() );
    }

    public Optional<RegistrationSystemModel> showAndWait() {
        Platform.runLater( () -> {
            nameField.requestFocus();
            nameField.focusedProperty().addListener( ( observable, oldValue, newValue ) -> disableButtons() );
        } );

        return dialog.showAndWait();
    }

    private void disableButtons() {
        Node okButton = dialog.getDialogPane().lookupButton( ButtonType.OK );
        if( okButton != null ) {
            okButton.setDisable( ControlUtils.getText( nameField ).isEmpty() );
        }
    }

    private RegistrationSystemModel convertControls( ButtonType buttonType ) {
        if( !buttonType.equals( ButtonType.OK ) ) {
            return null;
        }

        RegistrationSystemModel registrationSystemModel = new RegistrationSystemModel();
        registrationSystemModel.setId( model.getId() );
        registrationSystemModel.setName( nameField.getText() );
        registrationSystemModel.setDescription( descriptionField.getText() );

        return registrationSystemModel;
    }
}
