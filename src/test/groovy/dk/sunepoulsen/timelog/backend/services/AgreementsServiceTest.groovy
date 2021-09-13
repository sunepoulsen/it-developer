package dk.sunepoulsen.timelog.backend.services

import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import dk.sunepoulsen.timelog.testutils.persistence.TestDataHelper
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.time.LocalDate

class AgreementsServiceTest {
    private PersistenceStorage databaseStorage
    private TestDataHelper testDataHelper

    @BeforeClass
    static void migrateDatabase() {
        PersistenceStorage database = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        databaseStorage = new PersistenceStorage( "timelog", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        databaseStorage.connect()

        testDataHelper = new TestDataHelper(databaseStorage)
        testDataHelper.deleteAllData()
    }

    @After
    void clearDatabaseStorage() {
        databaseStorage.disconnect()
    }

    @Test
    void "Find agreement by date"() {
        // Given: Have existing agreements in the database
        testDataHelper.createAgreement('first', LocalDate.parse('2007-12-01'), LocalDate.parse('2008-06-30'), 7.6)
        testDataHelper.createAgreement('second', LocalDate.parse('2010-02-01'), 7.6)

        // Then: Find agreements by date
        AgreementsService agreementsService = new AgreementsService(databaseStorage)
        assert agreementsService.findByDate(LocalDate.parse('2007-11-30')).empty
        assert agreementsService.findByDate(LocalDate.parse('2007-12-01')).first().name == 'first'
        assert agreementsService.findByDate(LocalDate.parse('2008-06-30')).first().name == 'first'
        assert agreementsService.findByDate(LocalDate.parse('2008-07-01')).empty

        assert agreementsService.findByDate(LocalDate.parse('2010-02-01')).first().name == 'second'
        assert agreementsService.findByDate(LocalDate.parse('2048-08-22')).first().name == 'second'
    }
}
