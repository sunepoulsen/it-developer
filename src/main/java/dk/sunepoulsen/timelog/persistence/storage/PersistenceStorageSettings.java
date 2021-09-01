package dk.sunepoulsen.timelog.persistence.storage;

import dk.sunepoulsen.timelog.utils.os.OperatingSystem;
import dk.sunepoulsen.timelog.utils.os.OperatingSystemFactory;
import lombok.Data;

import java.io.IOException;
import java.util.Properties;

@Data
public class PersistenceStorageSettings {
    private static final String PROPERTY_PREFIX = "liquibase.datasource";

    private String driver;
    private String url;
    private String username;
    private String password;
    private String hibernateDialect;
    private Boolean showSql;
    private Boolean hibernateUseJdbcMetadataDefaults;

    public static PersistenceStorageSettings createInstanceFromProperties(Properties properties ) throws IOException {
        PersistenceStorageSettings instance = new PersistenceStorageSettings();

        String url = properties.getProperty( PROPERTY_PREFIX + ".jdbc.url" );
        if( url.contains( "${os.user.app.directory}" ) ) {
            OperatingSystem os = OperatingSystemFactory.getInstance();
            url = url.replace( "${os.user.app.directory}", os.applicationDataDirectory().getCanonicalPath() );
        }

        instance.setDriver( properties.getProperty( PROPERTY_PREFIX + ".driver" ) );
        instance.setUrl( url );
        instance.setUsername( properties.getProperty( PROPERTY_PREFIX + ".jdbc.user" ) );
        instance.setPassword( properties.getProperty( PROPERTY_PREFIX + ".jdbc.password" ) );
        instance.setHibernateDialect( properties.getProperty( PROPERTY_PREFIX + ".hibernate.dialect" ) );
        instance.setShowSql( Boolean.valueOf( properties.getProperty( PROPERTY_PREFIX + ".hibernate.show_sql" ) ) );
        instance.setHibernateUseJdbcMetadataDefaults( Boolean.valueOf( properties.getProperty( PROPERTY_PREFIX + ".hibernate.temp.use_jdbc_metadata_defaults" ) ) );

        return instance;
    }

    public static PersistenceStorageSettings createInstanceFromPropertyResource(String resourceName ) throws IOException {
        Properties props = new Properties();
        props.load( PersistenceStorageSettings.class.getResourceAsStream( resourceName ) );

        return createInstanceFromProperties( props );
    }
}
