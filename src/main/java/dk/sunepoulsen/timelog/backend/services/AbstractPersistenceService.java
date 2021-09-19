package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.AbstractEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.AbstractModel;
import dk.sunepoulsen.timelog.validation.TimeLogValidateException;
import dk.sunepoulsen.timelog.validation.TimeLogValidation;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractPersistenceService<M extends AbstractModel, T extends AbstractEntity> {
    protected final PersistenceStorage database;
    private final Class<T> entityClass;
    private final String findAllQueryName;
    private final String deleteQueryName;

    public AbstractPersistenceService(final PersistenceStorage database, Class<T> entityClass, String findAllQueryName, String deleteQueryName ) {
        this.database = database;
        this.entityClass = entityClass;
        this.findAllQueryName = findAllQueryName;
        this.deleteQueryName = deleteQueryName;
    }

    public M create(M model ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( model );

        database.transactional( em -> {
            T entity;

            try {
                entity = entityClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                log.error("Unable to generate instance of {}", entityClass.getName());
                throw new PersistenceServiceException(ex.getMessage(), ex);
            }

            entity = convertModel(entity, model );
            entity.setId(null);
            em.persist( entity );

            model.setId( entity.getId() );
        } );

        return model;
    }

    public M update( M model ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( model );

        database.transactional( em -> {
            T entity = em.find( entityClass, model.getId() );
            entity = convertModel( entity, model );
            em.persist( entity );

            model.setId( entity.getId() );
        } );

        return model;
    }

    public void delete( List<M> models) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( deleteQueryName );
            q.setParameter( "ids", models.stream().map(AbstractModel::getId).collect( Collectors.toList() ) );

            log.info( "Deleted {} {} entities", q.executeUpdate(), entityClass.getName() );
        } );
    }

    public M find( Long id ) {
        return database.untransactionalFunction( em -> {
            T entity = em.find( entityClass, id );
            return convertEntity( entity );
        } );
    }

    public List<M> findAll() {
        List<T> entities = database.query( em ->  em.createNamedQuery( findAllQueryName, entityClass ) );

        return entities.stream()
            .map( this::convertEntity )
            .collect( Collectors.toList() );
    }

    abstract T convertModel( T entity, M model );
    abstract M convertEntity( T entity );
}
