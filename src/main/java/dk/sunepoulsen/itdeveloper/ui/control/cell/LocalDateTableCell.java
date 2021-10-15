package dk.sunepoulsen.itdeveloper.ui.control.cell;

import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTableCell<S> extends TableCell<S, LocalDate> {
    private final DateTimeFormatter formatter;

    public LocalDateTableCell(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            setText(formatter.format(item));
        }
        else {
            setText(null);
        }

        setGraphic(null);
    }
}
