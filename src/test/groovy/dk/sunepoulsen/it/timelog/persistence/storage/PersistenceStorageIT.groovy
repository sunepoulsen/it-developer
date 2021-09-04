package dk.sunepoulsen.it.timelog.persistence.storage

import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

/**
 * Created by sunepoulsen on 12/05/2017.
 */
class PersistenceStorageIT {
    private PersistenceStorage databaseStorage

    @BeforeClass
    static void migrateDatabase() {
        PersistenceStorage database = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        databaseStorage = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        databaseStorage.connect()
        databaseStorage.deleteAllData()
    }

    @After
    void clearDatabaseStorage() {
        databaseStorage.disconnect()
    }
}
