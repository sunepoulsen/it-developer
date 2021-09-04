package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;

/**
 * Factory class to create service instances for an BackendConnection to access and update
 * the different concepts that makes an BackendConnection.
 */
public class ServicesFactory {
    private final PersistenceStorage database;

    public ServicesFactory( final PersistenceStorage database ) {
        this.database = database;
    }

    public RegistrationTypesService newRegistrationTypesService() {
        return new RegistrationTypesService( this.database );
    }

    public RegistrationReasonsService newRegistrationReasonsService() {
        return new RegistrationReasonsService( this.database );
    }
}
