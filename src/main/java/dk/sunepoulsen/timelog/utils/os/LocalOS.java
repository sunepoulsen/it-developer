package dk.sunepoulsen.timelog.utils.os;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LocalOS implements OperatingSystem {
    private Properties settings;

    LocalOS() throws IOException {
        loadSettings();
    }

    static boolean matchOperatingSystemName( String osName ) {
        return osName.equalsIgnoreCase( "localOS" );
    }

    @Override
    public File applicationDataDirectory() {
        return new File( System.getProperties().getProperty( "timelog.local.directory" ) );
    }

    private void loadSettings() throws IOException {
        this.settings = new Properties();
        this.settings.load( getClass().getResourceAsStream( "/application.properties" ) );
    }
}
