package dk.sunepoulsen.timelog.ui.dialogs.accounts;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.cells.RegistrationSystemListCell;
import dk.sunepoulsen.timelog.ui.model.accounts.AccountModel;
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModel;
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModelConverter;
import dk.sunepoulsen.timelog.utils.ControlUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by sunepoulsen on 11/06/2017.
 */
public class AccountDialog extends GridPane implements Initializable {
    private AccountModel model;

    @FXML
    private ComboBox<RegistrationSystemModel> registrationSystemField = null;

    @FXML
    private TextField nameField = null;

    @FXML
    private TextArea descriptionField = null;

    private Dialog<AccountModel> dialog = null;

    private Registry registry;
    private ResourceBundle bundle;

    public AccountDialog() {
        this( new AccountModel() );
    }

    public AccountDialog( AccountModel model ) {
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

        List<RegistrationSystemModel> registrationSystems = registry.getBackendConnection().servicesFactory().newRegistrationSystemsService().findAll();
        registrationSystemField.setItems( FXCollections.observableArrayList( registrationSystems ) );
        if( model != null ) {
            registrationSystems.stream()
                .filter( accountModel -> model.getRegistrationSystem() != null && accountModel.getId().equals( model.getRegistrationSystem().getId() ) )
                .findFirst()
                .ifPresent( rs -> registrationSystemField.getSelectionModel().select( rs ) );
        }
        registrationSystemField.setCellFactory( param -> new RegistrationSystemListCell() );
        registrationSystemField.setConverter( new RegistrationSystemModelConverter( registrationSystems ) );

        nameField.setText( model.getName() );
        nameField.textProperty().addListener( ( observable, oldValue, newValue ) -> disableButtons() );

        descriptionField.setText( model.getDescription() );
    }

    public Optional<AccountModel> showAndWait() {
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

    private AccountModel convertControls( ButtonType buttonType ) {
        if( !buttonType.equals( ButtonType.OK ) ) {
            return null;
        }

        AccountModel accountModel = new AccountModel();
        accountModel.setId( model.getId() );
        accountModel.setRegistrationSystem( registrationSystemField.getSelectionModel().getSelectedItem() );
        accountModel.setName( nameField.getText() );
        accountModel.setDescription( descriptionField.getText() );

        return accountModel;
    }
}
