package dk.sunepoulsen.itdeveloper.ui.control.cell;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class TableValueFactory<S,T> implements Callback<TableColumn.CellDataFeatures<S,T>, ObservableValue<T>> {
    private final Function<S, T> extractor;

    public TableValueFactory(Function<S, T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        return new ReadOnlyObjectWrapper<>(extractor.apply(param.getValue()));
    }
}
