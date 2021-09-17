package dk.sunepoulsen.timelog.ui.control.cell;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class TreeTableValueFactory<S,T> implements Callback<TreeTableColumn.CellDataFeatures<S,T>, ObservableValue<T>> {
    private final Function<S, T> extractor;

    public TreeTableValueFactory(Function<S, T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public ObservableValue<T> call(TreeTableColumn.CellDataFeatures<S, T> param) {
        return new ReadOnlyObjectWrapper<>(extractor.apply(param.getValue().getValue()));
    }
}
