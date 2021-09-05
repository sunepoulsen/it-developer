package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.AgreementEntity;
import dk.sunepoulsen.timelog.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.AgreementModel;
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
public class AgreementsService {
    private final PersistenceStorage database;

    public AgreementsService(final PersistenceStorage database ) {
        this.database = database;
    }

    public AgreementModel create(AgreementModel agreement ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( agreement );

        database.transactional( em -> {
            AgreementEntity entity = convertModel( new AgreementEntity(), agreement );
            em.persist( entity );

            agreement.setId( entity.getId() );
        } );

        return agreement;
    }

    public AgreementModel update( AgreementModel agreement ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( agreement );

        database.transactional( em -> {
            AgreementEntity entity = em.find( AgreementEntity.class, agreement.getId() );
            entity = convertModel( entity, agreement );
            em.persist( entity );

            agreement.setId( entity.getId() );
        } );

        return agreement;
    }

    public void delete( List<AgreementModel> agreements) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( "deleteAgreements" );
            q.setParameter( "ids", agreements.stream().map( AgreementModel::getId ).collect( Collectors.toList() ) );

            log.info( "Deleted {} agreements", q.executeUpdate() );
        } );
    }

    public AgreementModel find( Long id ) {
        return database.untransactionalFunction( em -> {
            AgreementEntity entity = em.find( AgreementEntity.class, id );
            return convertEntity( entity );
        } );
    }

    public List<AgreementModel> findAll() {
        List<AgreementEntity> entities = database.query( em ->  em.createNamedQuery( "findAllAgreements", AgreementEntity.class ) );

        return entities.stream()
                .map( AgreementsService::convertEntity )
                .collect( Collectors.toList() );
    }

    static AgreementEntity convertModel( AgreementEntity entity, AgreementModel model ) {
        entity.setId( model.getId() );
        entity.setName( model.getName() );
        entity.setStartDate( model.getStartDate() );
        entity.setEndDate( model.getEndDate() );
        entity.setMondayNorm( model.getMondayNorm() );
        entity.setTuesdayNorm( model.getTuesdayNorm() );
        entity.setWednesdayNorm( model.getWednesdayNorm() );
        entity.setThursdayNorm( model.getThursdayNorm() );
        entity.setFridayNorm( model.getFridayNorm() );
        entity.setSaturdayNorm( model.getSaturdayNorm() );
        entity.setSundayNorm( model.getSundayNorm() );

        return entity;
    }

    static AgreementModel convertEntity( AgreementEntity entity ) {
        AgreementModel model = new AgreementModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setStartDate( entity.getStartDate() );
        model.setEndDate( entity.getEndDate() );
        model.setMondayNorm( entity.getMondayNorm() );
        model.setTuesdayNorm( entity.getTuesdayNorm() );
        model.setWednesdayNorm( entity.getWednesdayNorm() );
        model.setThursdayNorm( entity.getThursdayNorm() );
        model.setFridayNorm( entity.getFridayNorm() );
        model.setSaturdayNorm( entity.getSaturdayNorm() );
        model.setSundayNorm( entity.getSundayNorm() );

        return model;
    }
}
