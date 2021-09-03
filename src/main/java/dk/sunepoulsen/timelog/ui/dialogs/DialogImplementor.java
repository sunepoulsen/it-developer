package dk.sunepoulsen.timelog.ui.dialogs;

import javafx.scene.control.ButtonType;

public interface DialogImplementor<T> {
    T convertControls(ButtonType buttonType );
}
