package dk.sunepoulsen.it.timelog.backend.services


import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

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

}
