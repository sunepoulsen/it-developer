package dk.sunepoulsen.timelog.ui.tasks.backend;


import dk.sunepoulsen.timelog.backend.BackendConnection;

import java.util.function.Consumer;

/**
 * Task to create a new RegistrationSystem in a BackendConnection
 */
public class ExecuteBackendServiceTask extends BackendConnectionTask<Void> {
    private Consumer<BackendConnection> executor;

    public ExecuteBackendServiceTask( BackendConnection connection, String performanceTag, Consumer<BackendConnection> executor ) {
        super( connection, performanceTag );
        this.executor = executor;
    }

    @Override
    protected Void call() throws Exception {
        executor.accept( connection );
        return null;
    }
}
