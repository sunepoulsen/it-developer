package dk.sunepoulsen.itdeveloper.ui.dialogs;

public interface DialogImplementor<T> {
    T toModel();
    void toControls();
}
