package dk.sunepoulsen.itdeveloper.persistence.storage

import dk.sunepoulsen.itdeveloper.testutils.persistence.TestDataHelper
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

import java.time.DayOfWeek
import java.time.LocalDate
import java.util.stream.Collectors
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
        PersistenceStorage database = new PersistenceStorage("it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource("/application-local.properties"))
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        persistenceStorage = new PersistenceStorage("it-developer", PersistenceStorageSettings.createInstanceFromPropertyResource("/application-local.properties"))
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
    void "Load test data"() {
        createAgreements()
        createRegistrationTypes()
        createProjectAccounts()
        createTimeLogs()
    }

    private void createAgreements() {
        LocalDate now = LocalDate.now()

        testDataHelper.createAgreement('Full time', LocalDate.of(now.year - 1, now.month, 1), 7.6)
    }

    private void createRegistrationTypes() {
        RegistrationTypeModel workType = testDataHelper.createRegistrationType('ARB', 'Arbejdet tid')
        testDataHelper.createRegistrationReason(workType, 'Project work')
        testDataHelper.createRegistrationReason(workType, 'Project meetings')
        testDataHelper.createRegistrationReason(workType, 'Refinement')
        testDataHelper.createRegistrationReason(workType, 'Internal meetings')

        testDataHelper.createRegistrationType('ASP', 'Afspadsering', false)
        testDataHelper.createRegistrationType('ATN', 'ATN', false)
        testDataHelper.createRegistrationType('BEV', 'Bevilget frihed', false)
        testDataHelper.createRegistrationType('BSY', 'Barns første sygedag', false)
        testDataHelper.createRegistrationType('FER', 'Ferie', false)
        testDataHelper.createRegistrationType('FERF', 'Flerårsaftale', false)
        testDataHelper.createRegistrationType('FERU', 'Ferie uden løn', false)
        testDataHelper.createRegistrationType('FLXA', 'Fleksfri-afviklingsaftale', false)
        testDataHelper.createRegistrationType('FRIL', 'Frihed med løntræk', false)
        testDataHelper.createRegistrationType('KUR', 'Kursus', false)
        testDataHelper.createRegistrationType('ODAG', '0-dag', false)
        testDataHelper.createRegistrationType('OMS', 'Omsorgsdage', false)
        testDataHelper.createRegistrationType('SFD', 'Særlig feriedage', false)
        testDataHelper.createRegistrationType('SYG', 'Sygdom', false)
        testDataHelper.createRegistrationType('TJFR', 'Beordret', false)
        testDataHelper.createRegistrationType('XSYG', 'Sygdom - rapp. af tidsadm', false)
        testDataHelper.createRegistrationType('ZADO', 'Adoptionsorlov', false)
        testDataHelper.createRegistrationType('ZASO', 'Arbejdsskade overenskomst', false)
        testDataHelper.createRegistrationType('ZAST', 'Arbejdsskade tjenestemænd', false)
        testDataHelper.createRegistrationType('ZBAR', 'Barselsorlov', false)
        testDataHelper.createRegistrationType('ZFOR', 'Forældreorlov', false)
        testDataHelper.createRegistrationType('ZFR1', 'Hospitalsindlæg. af barn', false)
        testDataHelper.createRegistrationType('ZFR2', 'Pasning af nær slægtning', false)
        testDataHelper.createRegistrationType('ZFR3', 'Fritstilling/Tjen.frihed', false)
        testDataHelper.createRegistrationType('ZFR4', 'Særlige hændelser', false)
        testDataHelper.createRegistrationType('ZFR5', 'Hovedopgave/Afhandling', false)
    }

    private void createProjectAccounts() {
        IntStream.range(1, 21).forEach {
            testDataHelper.createProjectAccount(String.format('ACC-%04d', it))
        }
    }

    private void createTimeLogs() {
        AgreementModel agreement = testDataHelper.findFirstAgreement()
        List<ProjectAccountModel> projectAccounts = testDataHelper.findProjectAccounts()

        RegistrationTypeModel registrationType = testDataHelper.findRegistrationType("ARB")
        for (LocalDate date = agreement.startDate; !date.isAfter(LocalDate.now()); date = date.plusDays(1)) {
            // Skip weekends
            if (date.dayOfWeek.value >= DayOfWeek.SATURDAY.value) {
                continue
            }

            testDataHelper.createTimeLog(date, registrationType, projectAccounts.stream()
                .filter(it -> Math.random() <= 0.5)
                .limit(3)
                .collect(Collectors.toList())
            )
        }
    }
}
