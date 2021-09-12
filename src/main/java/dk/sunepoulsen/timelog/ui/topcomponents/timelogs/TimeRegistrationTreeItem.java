package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.ui.control.LoadableTreeItem;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeRegistration;
import javafx.scene.control.TreeItem;

import java.util.Collection;
import java.util.stream.Collectors;

public class TimeRegistrationTreeItem extends LoadableTreeItem<TimeRegistration> {
    public TimeRegistrationTreeItem(TimeRegistration value) {
        super(value);
    }

    @Override
    protected Collection<? extends TreeItem<TimeRegistration>> loadChildren() {
        return getValue().children().stream()
            .map(TimeRegistrationTreeItem::new)
            .collect(Collectors.toList());
    }
}
