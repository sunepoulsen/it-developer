package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.backend.events.RegistrationSystemsEvents;
import dk.sunepoulsen.timelog.persistence.entities.RegistrationSystemEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.registration.systems.RegistrationSystemModel;
import dk.sunepoulsen.timelog.validation.TimeLogValidateException;
import dk.sunepoulsen.timelog.validation.TimeLogValidation;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunepoulsen on 12/06/2017.
 */
@Slf4j
public class RegistrationSystemsService {
    private final RegistrationSystemsEvents events;
    private final PersistenceStorage database;

    public RegistrationSystemsService( final RegistrationSystemsEvents events, final PersistenceStorage database ) {
        this.events = events;
        this.database = database;
    }

    public RegistrationSystemModel create( RegistrationSystemModel registrationSystem ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationSystem );

        database.transactional( em -> {
            RegistrationSystemEntity entity = convertModel( new RegistrationSystemEntity(), registrationSystem );
            em.persist( entity );

            registrationSystem.setId( entity.getId() );
        } );

        events.getCreatedEvent().fire( Arrays.asList( registrationSystem ) );
        return registrationSystem;
    }

    public RegistrationSystemModel update( RegistrationSystemModel registrationSystem ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( registrationSystem );

        database.transactional( em -> {
            RegistrationSystemEntity entity = em.find( RegistrationSystemEntity.class, registrationSystem.getId() );
            entity = convertModel( entity, registrationSystem );
            em.persist( entity );

            registrationSystem.setId( entity.getId() );
        } );

        events.getUpdatedEvent().fire( Arrays.asList( registrationSystem ) );
        return registrationSystem;
    }

    public void delete( List<RegistrationSystemModel> registrationSystems) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( "deleteRegistrationSystems" );
            q.setParameter( "ids", registrationSystems.stream().map( RegistrationSystemModel::getId ).collect( Collectors.toList() ) );

            log.info( "Deleted {} registration systems", q.executeUpdate() );
        } );

        log.debug( "Tricker event: RegistrationSystemsEvents::deletedEvent" );
        events.getDeletedEvent().fire( registrationSystems );
    }

    public RegistrationSystemModel find( Long id ) {
        return database.untransactionalFunction( em -> {
            RegistrationSystemEntity entity = em.find( RegistrationSystemEntity.class, id );
            return convertEntity( entity );
        } );
    }

    public List<RegistrationSystemModel> findAll() {
        List<RegistrationSystemEntity> entities = database.query( em ->  em.createNamedQuery( "findAllRegistrationSystems", RegistrationSystemEntity.class ) );

        return entities.stream()
                .map( RegistrationSystemsService::convertEntity )
                .collect( Collectors.toList() );
    }

    static RegistrationSystemEntity convertModel( RegistrationSystemEntity entity, RegistrationSystemModel model ) {
        entity.setId( model.getId() );
        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );

        return entity;
    }

    static RegistrationSystemModel convertEntity( RegistrationSystemEntity entity ) {
        RegistrationSystemModel model = new RegistrationSystemModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );

        return model;
    }
}
