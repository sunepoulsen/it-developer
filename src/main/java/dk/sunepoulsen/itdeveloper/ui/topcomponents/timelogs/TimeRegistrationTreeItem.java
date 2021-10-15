package dk.sunepoulsen.itdeveloper.ui.topcomponents.timelogs;

import dk.sunepoulsen.itdeveloper.ui.control.LoadableTreeItem;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeRegistration;
import javafx.scene.control.TreeItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TimeRegistrationTreeItem extends LoadableTreeItem<TimeRegistration> {
    public TimeRegistrationTreeItem(TimeRegistration value) {
        super(value);
    }

    @Override
    protected Collection<? extends TreeItem<TimeRegistration>> loadChildren() {
        List<TimeRegistration> children = getValue().children();

        if (children == null) {
            return Collections.emptyList();
        }

        return children.stream()
            .map(TimeRegistrationTreeItem::new)
            .collect(Collectors.toList());
    }
}
