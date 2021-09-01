package dk.sunepoulsen.it.timelog.persistence.storage

import dk.sunepoulsen.timelog.persistence.entities.AgreementEntity
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import javax.persistence.TypedQuery
import java.time.LocalDate

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

    @Test
    void findAgreement() {
        databaseStorage.untransactionalConsumer { em ->
            TypedQuery<AgreementEntity> query = em.createQuery( "SELECT a FROM AgreementEntity a WHERE a.name = 'IT Senior Developer'", AgreementEntity.class )
            List<AgreementEntity> list = query.getResultList()

            assert list != []
            assert list == [ new AgreementEntity( id: list.get( 0 ).id, name: "IT Senior Developer", startDate: LocalDate.parse( "2016-05-01" ) ) ]
        }
    }
}
