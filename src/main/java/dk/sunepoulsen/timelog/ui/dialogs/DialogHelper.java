package dk.sunepoulsen.timelog.ui.dialogs;

import dk.sunepoulsen.timelog.validation.TimeLogValidation;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.Getter;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogHelper<T> {
    private DialogImplementor<T> implementor;
    private Dialog<T> dialog;

    @Getter
    private Node okButton;

    public DialogHelper(DialogImplementor<T> implementor) {
        this.implementor = implementor;
    }

    public void initialize(ResourceBundle bundle) {
        dialog = new Dialog<>();
        dialog.setTitle( bundle.getString( "dialog.title.text" ) );
        dialog.setHeaderText( bundle.getString( "dialog.header.text" ) );

        dialog.getDialogPane().getButtonTypes().addAll( ButtonType.OK, ButtonType.CANCEL );
        dialog.getDialogPane().setContent( (Node)implementor );
        dialog.setResultConverter( this::convertControls );

        okButton = dialog.getDialogPane().lookupButton( ButtonType.OK );
        if( okButton != null ) {
            okButton.setDisable( true );
        }

        implementor.toControls();
    }

    public <R> void disableButtons(ObservableValue<? extends R> observable, R oldValue, R newValue) {
        okButton.setDisable(!new TimeLogValidation<T>().validate(implementor.toModel()).isEmpty());
    }

    public Optional<T> showAndWait(Node focusControl) {
        Platform.runLater( () -> focusControl.requestFocus() );
        return dialog.showAndWait();
    }

    private T convertControls(ButtonType buttonType) {
        if( !buttonType.equals( ButtonType.OK ) ) {
            return null;
        }

        return implementor.toModel();
    }

}
