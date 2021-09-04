package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
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
public class RegistrationTypesService {
    private final PersistenceStorage database;

    public RegistrationTypesService(final PersistenceStorage database ) {
        this.database = database;
    }

    public RegistrationTypeModel create(RegistrationTypeModel registrationType ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationType );

        database.transactional( em -> {
            RegistrationTypeEntity entity = convertModel( new RegistrationTypeEntity(), registrationType );
            em.persist( entity );

            registrationType.setId( entity.getId() );
        } );

        return registrationType;
    }

    public RegistrationTypeModel update( RegistrationTypeModel registrationSystem ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationSystem );

        database.transactional( em -> {
            RegistrationTypeEntity entity = em.find( RegistrationTypeEntity.class, registrationSystem.getId() );
            entity = convertModel( entity, registrationSystem );
            em.persist( entity );

            registrationSystem.setId( entity.getId() );
        } );

        return registrationSystem;
    }

    public void delete( List<RegistrationTypeModel> registrationTypes) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( "deleteRegistrationTypes" );
            q.setParameter( "ids", registrationTypes.stream().map( RegistrationTypeModel::getId ).collect( Collectors.toList() ) );

            log.info( "Deleted {} registration systems", q.executeUpdate() );
        } );
    }

    public RegistrationTypeModel find( Long id ) {
        return database.untransactionalFunction( em -> {
            RegistrationTypeEntity entity = em.find( RegistrationTypeEntity.class, id );
            return convertEntity( entity );
        } );
    }

    public List<RegistrationTypeModel> findAll() {
        List<RegistrationTypeEntity> entities = database.query( em ->  em.createNamedQuery( "findAllRegistrationTypes", RegistrationTypeEntity.class ) );

        return entities.stream()
                .map( RegistrationTypesService::convertEntity )
                .collect( Collectors.toList() );
    }

    static RegistrationTypeEntity convertModel( RegistrationTypeEntity entity, RegistrationTypeModel model ) {
        entity.setId( model.getId() );
        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );
        entity.setPurpose( model.getPurpose() );
        entity.setAllDay( model.isAllDay() );

        return entity;
    }

    static RegistrationTypeModel convertEntity( RegistrationTypeEntity entity ) {
        RegistrationTypeModel model = new RegistrationTypeModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );
        model.setAllDay( entity.getAllDay() );

        return model;
    }
}
