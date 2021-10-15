package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import javafx.concurrent.Task;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

public abstract class BackendConnectionTask<T> extends Task<T> {
    private final String performanceTag;
    private final StopWatch watch;
    protected final BackendConnection connection;

    public BackendConnectionTask( BackendConnection connection, String performanceTag ) {
        this.watch = new Log4JStopWatch();
        this.connection = connection;
        this.performanceTag = performanceTag;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        watch.stop(performanceTag + ".success");
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        watch.stop(performanceTag + ".cancelled");
    }

    @Override
    protected void failed() {
        super.failed();
        watch.stop(performanceTag + ".failed");
    }
}
