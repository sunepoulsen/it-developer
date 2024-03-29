package dk.sunepoulsen.itdeveloper.backend.services;

import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorage;

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

    public ProjectAccountsService newProjectAccountsService() {
        return new ProjectAccountsService( this.database );
    }

    public RegistrationReasonsService newRegistrationReasonsService(Long registrationTypeId) {
        return new RegistrationReasonsService( this.database, registrationTypeId );
    }

    public RegistrationTypesService newRegistrationTypesService() {
        return new RegistrationTypesService( this.database );
    }

    public TimeLogsService newTimeLogsService() {
        return new TimeLogsService( this.database );
    }
}
