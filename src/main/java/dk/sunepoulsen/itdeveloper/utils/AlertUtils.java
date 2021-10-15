package dk.sunepoulsen.itdeveloper.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtils {

    public static void notImplementedYet() {
        Alert alert = new Alert( Alert.AlertType.ERROR, "This operation is not implemented yet!", ButtonType.OK);
        alert.showAndWait();
    }

}
