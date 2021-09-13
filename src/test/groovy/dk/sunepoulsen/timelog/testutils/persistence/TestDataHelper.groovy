package dk.sunepoulsen.timelog.testutils.persistence

import dk.sunepoulsen.timelog.backend.services.AgreementsService
import dk.sunepoulsen.timelog.backend.services.RegistrationTypesService
import dk.sunepoulsen.timelog.backend.services.TimeLogsService
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage
import dk.sunepoulsen.timelog.ui.model.AgreementModel
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel

import java.time.LocalDate
import java.time.LocalTime

class TestDataHelper {
    private PersistenceStorage persistenceStorage

    TestDataHelper(PersistenceStorage persistenceStorage) {
        this.persistenceStorage = persistenceStorage
    }

    void deleteAllData() {
        persistenceStorage.transactional( em -> {
            em.createQuery( "DELETE FROM TimeLogEntity entity" ).executeUpdate();
            em.createQuery( "DELETE FROM RegistrationReasonEntity entity" ).executeUpdate();
            em.createQuery( "DELETE FROM RegistrationTypeEntity entity" ).executeUpdate();
            em.createQuery( "DELETE FROM ProjectAccountEntity entity" ).executeUpdate();
            em.createQuery( "DELETE FROM AgreementEntity entity" ).executeUpdate();
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

    RegistrationTypeModel createRegistrationType(String name, boolean allDay = false) {
        return new RegistrationTypesService(persistenceStorage).create(new RegistrationTypeModel(
            name: name,
            description: "description of ${name}",
            purpose: "purpose of ${name}",
            allDay: allDay
        ))
    }

    TimeLogModel createTimeLog(LocalDate date, LocalTime startTime, LocalTime endTime, RegistrationTypeModel registrationType) {
        return new TimeLogsService(persistenceStorage).create(new TimeLogModel(
            date: date,
            startTime: startTime,
            endTime: endTime,
            registrationType: registrationType
        ))
    }

    TimeLogModel createTimeLog(LocalDate date, RegistrationTypeModel registrationType) {
        return createTimeLog( date, TimeUtils.randomEntryTime(), TimeUtils.randomLeaveTime(), registrationType )
    }
}
