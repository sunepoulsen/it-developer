package dk.sunepoulsen.itdeveloper.registry;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunepoulsen on 18/05/2017.
 */
public class UIRegistry {
    //-------------------------------------------------------------------------
    //              Constructors
    //-------------------------------------------------------------------------

    public UIRegistry() {
        this.taskExecutorService = Executors.newSingleThreadExecutor();
    }

    public void initialize( final Stage primaryStage ) {
        this.stage = primaryStage;
    }

    public void shutdown() {
        this.taskExecutorService.shutdownNow();
    }

    //-------------------------------------------------------------------------
    //              Private members
    //-------------------------------------------------------------------------

    @Getter
    @Setter
    private Stage stage;

    @Getter
    private ExecutorService taskExecutorService;
}
