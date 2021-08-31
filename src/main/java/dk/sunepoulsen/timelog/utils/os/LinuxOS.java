package dk.sunepoulsen.timelog.utils.os;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LinuxOS implements OperatingSystem {
    private static final String APP_DATA_DIR_PATTERN = "%s/.timelog/%s/%s";

    private Properties settings;

    LinuxOS() throws IOException {
        loadSettings();
    }

    static boolean matchOperatingSystemName( String osName ) {
        return osName.equals( "Linux" );
    }

    @Override
    public File applicationDataDirectory() {
        String homeDir = System.getProperty( "user.home" );
        String applicationName = settings.getProperty( "application.name" );
        String applicationVersion = settings.getProperty( "application.version" );
        String appDataPath = String.format( APP_DATA_DIR_PATTERN, homeDir, applicationName, applicationVersion );

        return new File( appDataPath );
    }

    private void loadSettings() throws IOException {
        this.settings = new Properties();
        this.settings.load( getClass().getResourceAsStream( "/application.properties" ) );
    }
    }
