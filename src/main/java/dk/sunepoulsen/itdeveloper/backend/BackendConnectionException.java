package dk.sunepoulsen.itdeveloper.backend;

/**
 * Created by sunepoulsen on 09/06/2017.
 */
public class BackendConnectionException extends Exception {
    public BackendConnectionException( final String message, final Exception ex ) {
        super( message, ex );
    }
}
