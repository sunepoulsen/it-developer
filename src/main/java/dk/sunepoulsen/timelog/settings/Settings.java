package dk.sunepoulsen.timelog.settings;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sunepoulsen.timelog.settings.model.SettingsModel;
import dk.sunepoulsen.timelog.settings.model.UserStates;
import dk.sunepoulsen.timelog.utils.os.OperatingSystem;
import dk.sunepoulsen.timelog.utils.os.OperatingSystemFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * Created by sunepoulsen on 14/06/2017.
 */
@Data
@Slf4j
public class Settings {
    private SettingsModel model;
    private UserStates userStates;

    public void loadSettings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        this.model = mapper.readValue( getClass().getResourceAsStream( "/config/defaults.json" ), SettingsModel.class );
    }

    public void loadUserStates() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        OperatingSystem os = OperatingSystemFactory.getInstance();

        File file = new File( os.applicationDataDirectory().getAbsolutePath() + "/user-states.json" );
        if( file.exists() ) {
            this.userStates = mapper.readValue( file, UserStates.class );
        }

        if( userStates == null ) {
            this.userStates = new UserStates();
        }
    }

    public void storeUserStates() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        OperatingSystem os = OperatingSystemFactory.getInstance();

        File file = new File( os.applicationDataDirectory().getAbsolutePath() + "/user-states.json" );
        if( file.getParentFile().exists() || file.getParentFile().mkdirs() ) {
            log.debug( "Store user states to file: {}", file.getAbsolutePath() );
            mapper.writeValue( file, this.userStates );
        }
    }
}
