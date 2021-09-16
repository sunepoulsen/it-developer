package dk.sunepoulsen.timelog.persistence.storage

import dk.sunepoulsen.timelog.testutils.persistence.TestDataHelper
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

import java.time.LocalDate
import java.util.stream.IntStream

/**
 * This is not a test in the classical sense. It is mainly used to load data in the database that can be useful when
 * the developer run the application locally and needs some test data in the database.
 */
class DeveloperLoadTest {
    private PersistenceStorage persistenceStorage
    private TestDataHelper testDataHelper

    @BeforeClass
    static void migrateDatabase() {
        PersistenceStorage database = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-local.properties" ) )
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        persistenceStorage = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-local.properties" ) )
        persistenceStorage.connect()

        testDataHelper = new TestDataHelper(persistenceStorage)
        testDataHelper.deleteAllData()
    }

    @After
    void clearDatabaseStorage() {
        persistenceStorage.disconnect()
    }

    @Test
    @Ignore
    void "Load admin data"() {
        createAgreements()
        createRegistrationTypes()
        createProjectAccounts()
    }

    private void createAgreements() {
        LocalDate now = LocalDate.now()

        testDataHelper.createAgreement('Full time', LocalDate.of(now.year - 1, now.month, 1), 7.5)
    }

    private void createRegistrationTypes() {
        RegistrationTypeModel workType = testDataHelper.createRegistrationType('ARB', false)
        testDataHelper.createRegistrationReason(workType, 'Project meetings')
        testDataHelper.createRegistrationReason(workType, 'Refinement')
        testDataHelper.createRegistrationReason(workType, 'Internal meetings')

        testDataHelper.createRegistrationType('BEV', true)
        testDataHelper.createRegistrationType('SYG', true)
        testDataHelper.createRegistrationType('FER', true)
    }

    private void createProjectAccounts() {
        IntStream.range(1, 21).forEach {
            testDataHelper.createProjectAccount(String.format('ACC-%04d', it))
        }
    }

}
