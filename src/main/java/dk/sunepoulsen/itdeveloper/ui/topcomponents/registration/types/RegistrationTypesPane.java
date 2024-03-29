package dk.sunepoulsen.itdeveloper.ui.topcomponents.registration.types;

import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class RegistrationTypesPane extends BorderPane implements Initializable {
    @FXML
    private RegistrationTypesGroup registrationTypes;

    @FXML
    private RegistrationReasonsGroup registrationReasons;

    public RegistrationTypesPane() {
        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrationReasons.getCurrentRegistrationTypeProperty().bind( registrationTypes.getSelectedRegistrationTypeProperty() );
    }
}
