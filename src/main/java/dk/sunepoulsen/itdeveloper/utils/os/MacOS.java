package dk.sunepoulsen.itdeveloper.utils.os;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by sunepoulsen on 15/06/2017.
 */
public class MacOS implements OperatingSystem {
    private static final String APP_DATA_DIR_PATTERN = "%s/Library/Application Support/IT-Developer/%s";

    private Properties settings;

    MacOS() throws IOException {
        loadSettings();
    }

    static boolean matchOperatingSystemName( String osName ) {
        return osName.equals( "Mac OS X" );
    }

    @Override
    public File applicationDataDirectory() {
        String homeDir = System.getProperty( "user.home" );
        String applicationVersion = settings.getProperty( "application.version" );
        String appDataPath = String.format( APP_DATA_DIR_PATTERN, homeDir, applicationVersion );

        return new File( appDataPath );
    }

    private void loadSettings() throws IOException {
        this.settings = new Properties();
        this.settings.load( getClass().getResourceAsStream( "/application.properties" ) );
    }

}
