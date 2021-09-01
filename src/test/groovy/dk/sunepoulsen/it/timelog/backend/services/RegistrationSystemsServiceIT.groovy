package dk.sunepoulsen.it.timelog.backend.services

import dk.sunepoulsen.timelog.backend.events.RegistrationSystemsEvents
import dk.sunepoulsen.timelog.backend.services.RegistrationSystemsService
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModel
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class RegistrationSystemsServiceIT {
    private PersistenceStorage persistenceStorage

    @BeforeClass
    static void migratePersistence() {
        PersistenceStorage database = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        database.migrate()
    }

    @Before
    void initPersistenceStorage() {
        persistenceStorage = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        persistenceStorage.connect()
        persistenceStorage.deleteAllData()
    }

    @After
    void clearPersistenceStorage() {
        persistenceStorage.disconnect()
    }

    @Test
    void testCreateRegistrationSystem() {
        RegistrationSystemsService service = new RegistrationSystemsService( new RegistrationSystemsEvents(), persistenceStorage )

        RegistrationSystemModel model = new RegistrationSystemModel( name: "name", description: "description" )
        service.create( model )
        assert model.id != null

        RegistrationSystemModel actual = service.find( model.id )
        assert actual == new RegistrationSystemModel( id: model.id, name: "name", description: "description" )
    }
}
