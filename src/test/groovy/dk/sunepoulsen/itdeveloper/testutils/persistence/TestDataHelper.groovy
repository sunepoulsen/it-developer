package dk.sunepoulsen.itdeveloper.testutils.persistence

import dk.sunepoulsen.itdeveloper.backend.services.AgreementsService
import dk.sunepoulsen.itdeveloper.backend.services.ProjectAccountsService
import dk.sunepoulsen.itdeveloper.backend.services.RegistrationReasonsService
import dk.sunepoulsen.itdeveloper.backend.services.RegistrationTypesService
import dk.sunepoulsen.itdeveloper.backend.services.TimeLogsService
import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorage
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationReasonModel
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel

import java.time.LocalDate
import java.time.LocalTime

class TestDataHelper {
    private PersistenceStorage persistenceStorage

    TestDataHelper(PersistenceStorage persistenceStorage) {
        this.persistenceStorage = persistenceStorage
    }

    void deleteAllData() {
        persistenceStorage.transactional( em -> {
            em.createQuery( "DELETE FROM TimeLogEntity entity" ).executeUpdate()
            em.createQuery( "DELETE FROM RegistrationReasonEntity entity" ).executeUpdate()
            em.createQuery( "DELETE FROM RegistrationTypeEntity entity" ).executeUpdate()
            em.createQuery( "DELETE FROM ProjectAccountEntity entity" ).executeUpdate()
            em.createQuery( "DELETE FROM AgreementEntity entity" ).executeUpdate()
        } )
    }

    AgreementModel createAgreement(String name, LocalDate startDate, LocalDate endDate, Double weekDayNorm) {
        return new AgreementsService(persistenceStorage).create(new AgreementModel(
            name: name,
            startDate: startDate,
            endDate: endDate,
            mondayNorm: weekDayNorm,
            tuesdayNorm: weekDayNorm,
            wednesdayNorm: weekDayNorm,
            thursdayNorm: weekDayNorm,
            fridayNorm: weekDayNorm
        ))
    }

    AgreementModel createAgreement(String name, LocalDate startDate, Double weekDayNorm) {
        return createAgreement(name, startDate, null, weekDayNorm)
    }

    AgreementModel findFirstAgreement() {
        return new AgreementsService(persistenceStorage).findAll().first()
    }

    RegistrationTypeModel createRegistrationType(String name, boolean projectTime = true) {
        return new RegistrationTypesService(persistenceStorage).create(new RegistrationTypeModel(
            name: name,
            description: "description of ${name}",
            purpose: "purpose of ${name}",
            projectTime: projectTime
        ))
    }

    RegistrationTypeModel createRegistrationType(String name, String description, boolean projectTime = true) {
        return new RegistrationTypesService(persistenceStorage).create(new RegistrationTypeModel(
            name: name,
            description: description,
            purpose: '',
            projectTime: projectTime
        ))
    }

    RegistrationTypeModel findRegistrationType(String name) {
        return new RegistrationTypesService(persistenceStorage).findAll().stream()
            .filter {it.name == name}
            .findFirst()
            .orElse(null)
    }

    RegistrationReasonModel createRegistrationReason(RegistrationTypeModel registrationType, String name) {
        return new RegistrationReasonsService(persistenceStorage, registrationType.getId()).create(new RegistrationReasonModel(
            registrationTypeId: registrationType.id,
            name: name,
            description: "description of ${name}",
            purpose: "purpose of ${name}"
        ))
    }

    ProjectAccountModel createProjectAccount(String accountNumber) {
        return new ProjectAccountsService(persistenceStorage).create(new ProjectAccountModel(
            accountNumber: accountNumber,
            description: "description of ${accountNumber}",
            purpose: "purpose of ${accountNumber}"
        ))
    }

    List<ProjectAccountModel> findProjectAccounts() {
        return new ProjectAccountsService(persistenceStorage).findAll()
    }

    TimeLogModel createTimeLog(LocalDate date, LocalTime startTime, LocalTime endTime, RegistrationTypeModel registrationType, List<ProjectAccountModel> projectAccounts = []) {
        return new TimeLogsService(persistenceStorage).create(new TimeLogModel(
            date: date,
            startTime: startTime,
            endTime: endTime,
            registrationType: registrationType,
            projectAccounts: projectAccounts
        ))
    }

    TimeLogModel createTimeLog(LocalDate date, RegistrationTypeModel registrationType, List<ProjectAccountModel> projectAccounts = []) {
        return createTimeLog( date, TimeUtils.randomEntryTime(), TimeUtils.randomLeaveTime(), registrationType, projectAccounts )
    }

    TimeLogModel updateTimeLog(TimeLogModel model) {
        return new TimeLogsService(persistenceStorage).update(model)
    }
}
