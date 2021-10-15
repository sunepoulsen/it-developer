package dk.sunepoulsen.itdeveloper.ui.topcomponents.clientpane;

import dk.sunepoulsen.itdeveloper.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientPane extends AnchorPane {
    @Getter
    private SimpleObjectProperty<TreeNavigatorModel> currentPaneProperty;

    @FXML
    private StackPane stackPane = null;

    public ClientPane() {
        this.currentPaneProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        currentPaneProperty.addListener( ( observable, oldValue, newValue ) -> {
            if( newValue != null ) {
                showCurrentPane( newValue );
            }
        } );
    }

    private void showCurrentPane( TreeNavigatorModel newValue ) {
        Node node = newValue.getNode();
        stackPane.getChildren().setAll( node );
    }
}
