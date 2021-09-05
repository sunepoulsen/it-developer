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

    public AgreementsService newAgreementsService() {
        return new AgreementsService( this.database );
    }

    public RegistrationTypesService newRegistrationTypesService() {
        return new RegistrationTypesService( this.database );
    }

    public RegistrationReasonsService newRegistrationReasonsService(Long registrationTypeId) {
        return new RegistrationReasonsService( this.database, registrationTypeId );
    }
}
