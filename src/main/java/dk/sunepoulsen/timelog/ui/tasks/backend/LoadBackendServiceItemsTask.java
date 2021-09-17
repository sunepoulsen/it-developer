package dk.sunepoulsen.timelog.ui.tasks.backend;


import dk.sunepoulsen.timelog.backend.BackendConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.function.Function;

/**
 * Task to create a new RegistrationSystem in a BackendConnection
 */
public class LoadBackendServiceItemsTask<T> extends BackendConnectionTask<ObservableList<T>> {
    private Function<BackendConnection, Collection<? extends T>> collector;

    public LoadBackendServiceItemsTask( BackendConnection connection, String performanceTag, Function<BackendConnection, Collection<? extends T>> collector ) {
        super( connection, performanceTag );
        this.collector = collector;
    }

    @Override
    protected ObservableList<T> call() throws Exception {
        return FXCollections.observableArrayList( collector.apply( connection ) );
    }
}
