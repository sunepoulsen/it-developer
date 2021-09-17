package dk.sunepoulsen.timelog.ui.control;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

import java.util.Collection;

public abstract class LoadableTreeItem<T> extends TreeItem<T> {
    private boolean invalidated = true;

    public LoadableTreeItem() {
        super();
    }

    public LoadableTreeItem(T value) {
        super(value);
    }

    public LoadableTreeItem(T value, Node graphic) {
        super(value, graphic);
    }

    @Override
    public boolean isLeaf() {
        loadAndSetChildrenIfInvalidated();
        return super.isLeaf();
    }

    @Override
    public ObservableList<TreeItem<T>> getChildren() {
        loadAndSetChildrenIfInvalidated();
        return super.getChildren();
    }

    protected abstract Collection<? extends TreeItem<T>> loadChildren();

    private void loadAndSetChildrenIfInvalidated() {
        if( invalidated ) {
            super.getChildren().setAll(loadChildren());
            invalidated = false;
        }
    }
}
