package dk.sunepoulsen.itdeveloper.ui.mainwindow;

import com.google.common.primitives.Doubles;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.clientpane.ClientPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.navigator.TreeNavigator;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.utils.ApplicationConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

@Slf4j
public class MainWindow implements Initializable {
    @FXML
    private SplitPane splitPane;

    @FXML
    private TreeNavigator navigator;

    @FXML
    private ClientPane clientPane;

    @FXML
    private Label appVersionLabel;

    boolean splitPaneInitialized = false;

    @Override
    public void initialize( final URL location, final ResourceBundle resources ) {
        clientPane.getCurrentPaneProperty().bind( navigator.getSelectedProperty() );
        appVersionLabel.setText(ApplicationConfig.loadConfig().getProperty("application.version"));
    }

    public void initializeSplitPaneDividerPosition( WindowEvent windowEvent ) {
        log.debug( "Initialize divider position of SplitPane. Old positions: {}", splitPane.getDividerPositions() );

        if( windowEvent.getEventType().equals( WindowEvent.WINDOW_SHOWN ) && !splitPaneInitialized ) {
            double[] newPositions = { 0.20 };
            List<Double> userPositions = Registry.getDefault().getSettings().getUserStates().getDividerPositions();

            if( userPositions != null && !userPositions.isEmpty() ) {
                newPositions = Doubles.toArray( userPositions );
            }

            log.debug( "Set divider position to {}. Old value: {}", newPositions, splitPane.getDividerPositions() );
            splitPane.setDividerPositions( newPositions );
            splitPaneInitialized = true;
        }
    }

    public void storeSplitPaneDividerPosition( WindowEvent windowEvent ) {
        if( windowEvent.getEventType().equals( WindowEvent.WINDOW_CLOSE_REQUEST ) ) {
            log.debug( "Store divider positions of SplitPane: {}", splitPane.getDividerPositions() );

            List<Double> positions = Doubles.asList( splitPane.getDividerPositions() );
            Registry.getDefault().getSettings().getUserStates().setDividerPositions( positions );
        }
    }
}
