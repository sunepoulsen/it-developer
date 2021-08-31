package dk.sunepoulsen.timelog.utils.os;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by sunepoulsen on 15/06/2017.
 */
public class OperatingSystemFactory {
    private static OperatingSystem operatingSystem = null;

    public static OperatingSystem getInstance() throws IOException {
        if( operatingSystem == null ) {
            operatingSystem = createOperatingSystem();
        }

        return operatingSystem;
    }

    private static OperatingSystem createOperatingSystem() throws IOException {
        return createOperatingSystem( System.getProperties() );
    }

    private static OperatingSystem createOperatingSystem( Properties properties ) throws IOException {
        String osName = properties.getProperty( "os.name" );
        if( properties.containsKey( "timelog.os.name" ) ) {
            osName = properties.getProperty( "timelog.os.name" );
        }

        if( LocalOS.matchOperatingSystemName( osName ) ) {
            return new LocalOS();
        }
        else if( MacOS.matchOperatingSystemName( osName ) ) {
            return new MacOS();
        }
        else if( LinuxOS.matchOperatingSystemName( osName ) ) {
            return new LinuxOS();
        }

        throw new IOException( "Unsupported operating system: " + osName );
    }
}
