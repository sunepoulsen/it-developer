package dk.sunepoulsen.timelog.ui.dialogs;

import dk.sunepoulsen.timelog.validation.TimeLogValidation;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.Getter;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogHelper<T> {
    private Dialog<T> dialog;

    @Getter
    private Node okButton;

    public void initialize(DialogImplementor<T> implementor, ResourceBundle bundle) {
        dialog = new Dialog<>();
        dialog.setTitle( bundle.getString( "dialog.title.text" ) );
        dialog.setHeaderText( bundle.getString( "dialog.header.text" ) );

        dialog.getDialogPane().getButtonTypes().addAll( ButtonType.OK, ButtonType.CANCEL );
        dialog.getDialogPane().setContent( (Node)implementor );
        dialog.setResultConverter( implementor::convertControls );

        okButton = dialog.getDialogPane().lookupButton( ButtonType.OK );
        if( okButton != null ) {
            okButton.setDisable( true );
        }
    }

    public void disableButtons(T model) {
        okButton.setDisable(!new TimeLogValidation<T>().validate(model).isEmpty());
    }

    public Optional<T> showAndWait() {
        return dialog.showAndWait();
    }

}
