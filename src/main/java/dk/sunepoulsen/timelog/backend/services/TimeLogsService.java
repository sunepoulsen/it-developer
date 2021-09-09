package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.RegistrationReasonEntity;
import dk.sunepoulsen.timelog.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.timelog.persistence.entities.TimeLogEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TimeLogsService extends AbstractPersistenceService<TimeLogModel, TimeLogEntity>{
    public TimeLogsService(final PersistenceStorage database ) {
        super(database, TimeLogEntity.class, "findAllTimeLogs", "deleteTimeLogs");
    }

    public List<TimeLogModel> findByDates(LocalDate from, LocalDate to) {
        List<TimeLogEntity> entities = database.query( em -> {
            TypedQuery<TimeLogEntity> q = em.createNamedQuery( "findByDates", TimeLogEntity.class );
            q.setParameter("from", from);
            q.setParameter("to", to);

            return q;
        } );

        return entities.stream()
            .map( this::convertEntity )
            .collect( Collectors.toList() );
    }

    @Override
    protected TimeLogEntity convertModel( TimeLogEntity entity, TimeLogModel model ) {
        entity.setId( model.getId() );
        entity.setDate( model.getDate() );
        entity.setStartTime( model.getStartTime() );
        entity.setEndTime( model.getEndTime() );

        if (model.getRegistrationTypeId() != null) {
            RegistrationTypeEntity registrationTypeEntity = new RegistrationTypeEntity();
            registrationTypeEntity.setId(model.getRegistrationTypeId());

            entity.setRegistrationType(registrationTypeEntity);
        }

        if (model.getRegistrationReasonId() != null) {
            RegistrationReasonEntity registrationReasonEntity = new RegistrationReasonEntity();
            registrationReasonEntity.setId(model.getRegistrationReasonId());

            entity.setRegistrationReason(registrationReasonEntity);
        }

        return entity;
    }

    @Override
    protected TimeLogModel convertEntity( TimeLogEntity entity ) {
        TimeLogModel model = new TimeLogModel();

        model.setId( entity.getId() );
        model.setDate( entity.getDate() );
        model.setStartTime( entity.getStartTime() );
        model.setEndTime( entity.getEndTime() );

        if (entity.getRegistrationType() != null) {
            model.setRegistrationTypeId(entity.getRegistrationType().getId());
        }
        if (entity.getRegistrationReason() != null) {
            model.setRegistrationReasonId(entity.getRegistrationReason().getId());
        }

        return model;
    }
}
