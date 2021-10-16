package dk.sunepoulsen.itdeveloper.application;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.mainwindow.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Locale;

public class ITDeveloperApplication extends Application {
    private Registry registry = Registry.getDefault();

    @Override
    public void start( final Stage primaryStage ) throws Exception {
        Locale.setDefault(new Locale("en", "DK"));
        registry.initialize( primaryStage );

        FXMLLoader loader = new FXMLLoader( MainWindow.class.getResource( "mainwindow.fxml" ) );
        Parent root = loader.load();
        MainWindow mainWindow = loader.getController();

        Scene scene = new Scene( root );
        scene.getStylesheets().add("/styles/app.css");

        primaryStage.setTitle( "IT Developer" );
        primaryStage.setScene( scene );
        scene.windowProperty().getValue().setOnShown( mainWindow::initializeSplitPaneDividerPosition );
        scene.windowProperty().getValue().setOnCloseRequest( mainWindow::storeSplitPaneDividerPosition );

        // Maximize the window
        final Screen screen = Screen.getPrimary();

        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        double width = screenWidth / 1.5;
        double height = screenHeight / 1.5;
        primaryStage.setX( ( screenWidth - width ) / 2.0 );
        primaryStage.setY( ( screenHeight - height ) / 2.0 );
        primaryStage.setWidth( width );
        primaryStage.setHeight( height );

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        registry.shutdown();
        super.stop();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        launch( args );
    }
}
