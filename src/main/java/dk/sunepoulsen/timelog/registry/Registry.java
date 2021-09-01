//-----------------------------------------------------------------------------
package dk.sunepoulsen.timelog.registry;

//-----------------------------------------------------------------------------

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.backend.BackendConnectionException;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings;
import dk.sunepoulsen.timelog.settings.Settings;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by sunepoulsen on 21/10/2016.
 */
@Slf4j
public class Registry {
    //-------------------------------------------------------------------------
    //              Constructors
    //-------------------------------------------------------------------------

    private Registry() {
        this.uiRegistry = new UIRegistry();
        this.locale = null;
        this.persistenceStorageSettings = null;
        this.settings = new Settings();
        this.backendConnection = null;
    }

    public void initialize( final Stage primaryStage ) throws IOException, BackendConnectionException {
        this.backendConnection = new BackendConnection( PersistenceStorageSettings.createInstanceFromPropertyResource( "/application.properties" ) );
        this.backendConnection.connect();

        this.uiRegistry.initialize( primaryStage );
        this.locale = Locale.getDefault();

        this.settings.loadSettings();
        this.settings.loadUserStates();
    }

    public void shutdown() {
        this.uiRegistry.shutdown();

        try {
            settings.storeUserStates();
        }
        catch( IOException ex ) {
            log.warn( "Unable to store user states", ex );
        }

        this.backendConnection.disconnect();
    }

    //-------------------------------------------------------------------------
    //              Resource Bundles
    //-------------------------------------------------------------------------

    public <T> ResourceBundle getBundle( Class<T> clazz ) {
        String baseName = clazz.getName().toLowerCase();
        return ResourceBundle.getBundle( baseName, locale );
    }

    //-------------------------------------------------------------------------
    //              Global registry
    //-------------------------------------------------------------------------

    public static Registry getDefault() {
        if( global == null ) {
            global = new Registry();
        }

        return global;
    }

    //-------------------------------------------------------------------------
    //              Private members
    //-------------------------------------------------------------------------

    private static Registry global;

    @Getter
    private BackendConnection backendConnection;

    @Getter
    @Setter
    private Locale locale;

    private PersistenceStorageSettings persistenceStorageSettings;

    @Getter
    @Setter
    private Settings settings;

    @Getter
    private UIRegistry uiRegistry;
}
