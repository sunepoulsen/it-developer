package dk.sunepoulsen.itdeveloper.ui.topcomponents.overview;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YearViewerPane extends AnchorPane {
    @Getter
    private SimpleObjectProperty<Integer> currentYearProperty;

    public YearViewerPane() {
        this.currentYearProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(Registry.getDefault().getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );
    }
}
