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
        PersistenceStorage database = new PersistenceStorage("timelog", PersistenceStorageSettings.createInstanceFromPropertyResource("/application-local.properties"))
        database.migrate()
    }

    @Before
    void initDatabaseStorage() {
        persistenceStorage = new PersistenceStorage("timelog", PersistenceStorageSettings.createInstanceFromPropertyResource("/application-local.properties"))
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
        RegistrationTypeModel workType = testDataHelper.createRegistrationType('ARB', 'Arbejdet tid', false)
        testDataHelper.createRegistrationReason(workType, 'Project work')
        testDataHelper.createRegistrationReason(workType, 'Project meetings')
        testDataHelper.createRegistrationReason(workType, 'Refinement')
        testDataHelper.createRegistrationReason(workType, 'Internal meetings')

        testDataHelper.createRegistrationType('ASP', 'Afspadsering', true)
        testDataHelper.createRegistrationType('ATN', 'ATN', true)
        testDataHelper.createRegistrationType('BEV', 'Bevilget frihed', true)
        testDataHelper.createRegistrationType('BSY', 'Barns første sygedag', true)
        testDataHelper.createRegistrationType('FER', 'Ferie', true)
        testDataHelper.createRegistrationType('FERF', 'Flerårsaftale', true)
        testDataHelper.createRegistrationType('FERU', 'Ferie uden løn', true)
        testDataHelper.createRegistrationType('FLXA', 'Fleksfri-afviklingsaftale', true)
        testDataHelper.createRegistrationType('FRIL', 'Frihed med løntræk', true)
        testDataHelper.createRegistrationType('KUR', 'Kursus', true)
        testDataHelper.createRegistrationType('ODAG', '0-dag', true)
        testDataHelper.createRegistrationType('OMS', 'Omsorgsdage', true)
        testDataHelper.createRegistrationType('SFD', 'Særlig feriedage', true)
        testDataHelper.createRegistrationType('SYG', 'Sygdom', true)
        testDataHelper.createRegistrationType('TJFR', 'Beordret', true)
        testDataHelper.createRegistrationType('XSYG', 'Sygdom - rapp. af tidsadm', true)
        testDataHelper.createRegistrationType('ZADO', 'Adoptionsorlov', true)
        testDataHelper.createRegistrationType('ZASO', 'Arbejdsskade overenskomst', true)
        testDataHelper.createRegistrationType('ZAST', 'Arbejdsskade tjenestemænd', true)
        testDataHelper.createRegistrationType('ZBAR', 'Barselsorlov', true)
        testDataHelper.createRegistrationType('ZFOR', 'Forældreorlov', true)
        testDataHelper.createRegistrationType('ZFR1', 'Hospitalsindlæg. af barn', true)
        testDataHelper.createRegistrationType('ZFR2', 'Pasning af nær slægtning', true)
        testDataHelper.createRegistrationType('ZFR3', 'Fritstilling/Tjen.frihed', true)
        testDataHelper.createRegistrationType('ZFR4', 'Særlige hændelser', true)
        testDataHelper.createRegistrationType('ZFR5', 'Hovedopgave/Afhandling', true)
    }

    private void createProjectAccounts() {
        IntStream.range(1, 21).forEach {
            testDataHelper.createProjectAccount(String.format('ACC-%04d', it))
        }
    }

}
