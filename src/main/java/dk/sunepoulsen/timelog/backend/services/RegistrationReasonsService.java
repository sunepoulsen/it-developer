package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.RegistrationReasonEntity;
import dk.sunepoulsen.timelog.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.timelog.validation.TimeLogValidateException;
import dk.sunepoulsen.timelog.validation.TimeLogValidation;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunepoulsen on 12/06/2017.
 */
@Slf4j
public class RegistrationReasonsService {
    private final PersistenceStorage database;

    public RegistrationReasonsService(final PersistenceStorage database ) {
        this.database = database;
    }

    public RegistrationReasonModel create(RegistrationReasonModel registrationReason ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationReason );

        database.transactional( em -> {
            RegistrationReasonEntity entity = convertModel( new RegistrationReasonEntity(), registrationReason );
            em.persist( entity );

            registrationReason.setId( entity.getId() );
        } );

        return registrationReason;
    }

    public RegistrationReasonModel update( RegistrationReasonModel registrationReason ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationReason );

        database.transactional( em -> {
            RegistrationReasonEntity entity = em.find( RegistrationReasonEntity.class, registrationReason.getId() );
            entity = convertModel( entity, registrationReason );
            em.persist( entity );

            registrationReason.setId( entity.getId() );
        } );

        return registrationReason;
    }

    public void delete( List<RegistrationReasonModel> registrationReasons) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( "deleteRegistrationReasons" );
            q.setParameter( "ids", registrationReasons.stream().map( RegistrationReasonModel::getId ).collect( Collectors.toList() ) );

            log.info( "Deleted {} registration reasons", q.executeUpdate() );
        } );
    }

    public RegistrationReasonModel find( Long id ) {
        return database.untransactionalFunction( em -> {
            RegistrationReasonEntity entity = em.find( RegistrationReasonEntity.class, id );
            return convertEntity( entity );
        } );
    }

    public List<RegistrationReasonModel> findAll(Long registrationTypeId) {
        List<RegistrationReasonEntity> entities = database.query( em ->
            em.createNamedQuery( "findAllRegistrationReasons", RegistrationReasonEntity.class )
            .setParameter("registration_type_id", registrationTypeId)
        );

        return entities.stream()
                .map( RegistrationReasonsService::convertEntity )
                .collect( Collectors.toList() );
    }

    static RegistrationReasonEntity convertModel( RegistrationReasonEntity entity, RegistrationReasonModel model ) {
        entity.setId( model.getId() );

        RegistrationTypeEntity registrationTypeEntity = new RegistrationTypeEntity();
        registrationTypeEntity.setId(model.getRegistrationTypeId());
        entity.setRegistrationType(registrationTypeEntity);

        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );
        entity.setPurpose( model.getPurpose() );

        return entity;
    }

    static RegistrationReasonModel convertEntity( RegistrationReasonEntity entity ) {
        RegistrationReasonModel model = new RegistrationReasonModel();

        model.setId( entity.getId() );
        model.setRegistrationTypeId(entity.getRegistrationType().getId());
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );

        return model;
    }
}
