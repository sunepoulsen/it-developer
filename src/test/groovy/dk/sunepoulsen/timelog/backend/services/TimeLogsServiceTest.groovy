package dk.sunepoulsen.timelog.backend.services

import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorageSettings
import dk.sunepoulsen.timelog.testutils.persistence.TestDataHelper
import dk.sunepoulsen.timelog.testutils.persistence.TimeUtils
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass
import org.junit.Test

import java.time.LocalDate

class TimeLogsServiceTest {
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
    void "Create new timelog"() {
        // Given: Have existing registration type in the database
        RegistrationTypeModel registrationType = testDataHelper.createRegistrationType('ARB')

        // Then: Create a new timelog with the created registration id
        assert testDataHelper.createTimeLog(LocalDate.now(), registrationType).getId() > 0
    }

    @Test
    void "Find timelogs between dates"() {
        // Given: Have existing registration type in the database
        RegistrationTypeModel registrationType = testDataHelper.createRegistrationType('ARB')

        // Given: Two timelogs on yesterday
        testDataHelper.createTimeLog(TimeUtils.yesterday(), registrationType )
        testDataHelper.createTimeLog(TimeUtils.yesterday(), registrationType )

        // Given: One timelogs from today
        Long expectedTimeLogId = testDataHelper.createTimeLog(LocalDate.now(), registrationType ).getId()

        // Given: Two timelogs on tomorrow
        testDataHelper.createTimeLog(TimeUtils.tomorrow(), registrationType )
        testDataHelper.createTimeLog(TimeUtils.tomorrow(), registrationType )

        // When: Find the timelogs from today
        List<TimeLogModel> results = new TimeLogsService(databaseStorage).findByDates(LocalDate.now(), LocalDate.now().plusDays(1))

        // Then
        assert results.size() == 1
        assert results.first().getId() == expectedTimeLogId
    }
}
