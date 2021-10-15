package dk.sunepoulsen.itdeveloper.ui.control.cell;

import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel;
import javafx.scene.control.TableCell;

public class RegistrationTypeTableCell<S> extends TableCell<S, RegistrationTypeModel> {
    @Override
    protected void updateItem(RegistrationTypeModel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        setText(item.getName());
    }
}
