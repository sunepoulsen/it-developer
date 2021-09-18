package dk.sunepoulsen.timelog.ui.control.cell;

import dk.sunepoulsen.timelog.ui.model.reporting.TimeReportingInterval;
import javafx.scene.control.TableCell;

import java.time.format.DateTimeFormatter;

public class TimeReportingIntervalTableCell<S> extends TableCell<S, TimeReportingInterval> {
    private DateTimeFormatter timeFormatter;

    public TimeReportingIntervalTableCell(DateTimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    @Override
    protected void updateItem(TimeReportingInterval item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        setText(String.format("%s - %s", timeFormatter.format(item.getStartTime()), timeFormatter.format(item.getEndTime())));
    }
}
