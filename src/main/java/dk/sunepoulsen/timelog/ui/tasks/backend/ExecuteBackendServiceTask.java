package dk.sunepoulsen.timelog.ui.tasks.backend;


import dk.sunepoulsen.timelog.backend.BackendConnection;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Task to create a new RegistrationSystem in a BackendConnection
 */
@Slf4j
public class ExecuteBackendServiceTask extends BackendConnectionTask<Void> {
    private Consumer<BackendConnection> executor;

    public ExecuteBackendServiceTask( BackendConnection connection, String performanceTag, Consumer<BackendConnection> executor ) {
        super( connection, performanceTag );
        this.executor = executor;
    }

    @Override
    protected Void call() throws Exception {
        try {
            executor.accept(connection);
            return null;
        }
        catch( Exception ex) {
            log.info("Unable to execute task {}: {}", getClass().getName(), ex.getMessage());
            log.debug("Exception", ex);
            throw ex;
        }
    }
}
