package dk.sunepoulsen.itdeveloper.persistence.storage

import dk.sunepoulsen.itdeveloper.persistence.entities.AgreementEntity
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

/**
 * Created by sunepoulsen on 12/05/2017.
 */
class PersistenceStorageTest {
    private PersistenceStorage databaseStorage

    @BeforeClass
    static void migrateDatabase() {
        PersistenceStorage database = new PersistenceStorage( "it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        databaseStorage = new PersistenceStorage( "it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        databaseStorage.connect()
        databaseStorage.deleteAllData()
    }

    @After
    void clearDatabaseStorage() {
        databaseStorage.disconnect()
    }

    @Test
    void "Find all agreements in new database"() {
        assert databaseStorage.query { em ->
                em.createNamedQuery('findAllAgreements', AgreementEntity)
            }.empty
    }
}
