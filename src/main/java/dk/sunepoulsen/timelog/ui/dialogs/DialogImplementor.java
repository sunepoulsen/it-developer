package dk.sunepoulsen.timelog.ui.dialogs;

public interface DialogImplementor<T> {
    T toModel();
    void toControls();
}
