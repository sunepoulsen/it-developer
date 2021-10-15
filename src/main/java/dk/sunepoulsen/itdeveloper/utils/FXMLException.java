package dk.sunepoulsen.itdeveloper.utils;

public class FXMLException extends RuntimeException {
    public FXMLException(String message, Throwable cause) {
        super(message, cause);
    }

    public FXMLException(Throwable cause) {
        this("Unable to configure FXML", cause);
    }
}
