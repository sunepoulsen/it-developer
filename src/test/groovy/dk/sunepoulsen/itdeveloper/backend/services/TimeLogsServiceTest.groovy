package dk.sunepoulsen.itdeveloper.backend.services


import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorage
import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorageSettings
import dk.sunepoulsen.itdeveloper.testutils.persistence.TestDataHelper
import dk.sunepoulsen.itdeveloper.testutils.persistence.TimeUtils
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.time.LocalDate

class TimeLogsServiceTest {
    private PersistenceStorage databaseStorage
    private TestDataHelper testDataHelper

    @BeforeClass
    static void migrateDatabase() {
        PersistenceStorage database = new PersistenceStorage( "it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        databaseStorage = new PersistenceStorage( "it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource( "/application-test.properties" ) )
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
    void "Create new timelog with project accounts"() {
        // Given: Have existing registration type in the database
        RegistrationTypeModel registrationType = testDataHelper.createRegistrationType('ARB')

        // Given: Have existing project accounts in the database
        List<ProjectAccountModel> projectAccounts = [
            testDataHelper.createProjectAccount("ACC-001"),
            testDataHelper.createProjectAccount("ACC-002"),
            testDataHelper.createProjectAccount("ACC-003")
        ]

        // When: Create new timelog with two project accounts
        TimeLogModel result = testDataHelper.createTimeLog(LocalDate.now(), registrationType, [projectAccounts[0], projectAccounts[2]])

        // Then
        assert result.getId() > 0
        assert result.projectAccounts.size() == 2
        assert result.projectAccounts[0].accountNumber == projectAccounts[0].accountNumber
        assert result.projectAccounts[1].accountNumber == projectAccounts[2].accountNumber
    }

    @Test
    void "Update timelog with new project accounts"() {
        // Given: Have existing registration type in the database
        RegistrationTypeModel registrationType = testDataHelper.createRegistrationType('ARB')

        // Given: Have existing project accounts in the database
        List<ProjectAccountModel> projectAccounts = [
            testDataHelper.createProjectAccount("ACC-001"),
            testDataHelper.createProjectAccount("ACC-002"),
            testDataHelper.createProjectAccount("ACC-003")
        ]

        // Given: Existing timelog with project accounts
        TimeLogModel timeLogModel = testDataHelper.createTimeLog(LocalDate.now(), registrationType, [projectAccounts[0], projectAccounts[2]])

        // When: Update timelog with new project account
        timeLogModel.projectAccounts = [projectAccounts[1]]
        TimeLogModel result = testDataHelper.updateTimeLog(timeLogModel)

        // Then
        assert result.getId() == timeLogModel.id
        assert result.projectAccounts.size() == 1
        assert result.projectAccounts[0].accountNumber == projectAccounts[1].accountNumber
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
        List<TimeLogModel> results = new TimeLogsService(databaseStorage).findByDates(LocalDate.now(), LocalDate.now())

        // Then
        assert results.size() == 1
        assert results.first().getId() == expectedTimeLogId
    }

}
