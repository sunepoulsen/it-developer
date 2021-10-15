package dk.sunepoulsen.itdeveloper.ui.control.cell;

import dk.sunepoulsen.itdeveloper.ui.model.reporting.TimeRegistrationReportingInterval;
import javafx.scene.control.TableCell;

import java.time.format.DateTimeFormatter;

public class TimeReportingIntervalTableCell<S> extends TableCell<S, TimeRegistrationReportingInterval> {
    private DateTimeFormatter timeFormatter;

    public TimeReportingIntervalTableCell(DateTimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    @Override
    protected void updateItem(TimeRegistrationReportingInterval item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        setText(String.format("%s - %s", timeFormatter.format(item.getStartTime()), timeFormatter.format(item.getEndTime())));
    }
}
