package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sunepoulsen on 12/06/2017.
 */
@Slf4j
public class RegistrationTypesService extends AbstractPersistenceService<RegistrationTypeModel, RegistrationTypeEntity> {
    public RegistrationTypesService(final PersistenceStorage database ) {
        super(database, RegistrationTypeEntity.class, "findAllRegistrationTypes", "deleteRegistrationTypes");
    }

    @Override
    RegistrationTypeEntity convertModel( RegistrationTypeEntity entity, RegistrationTypeModel model ) {
        entity.setId( model.getId() );
        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );
        entity.setPurpose( model.getPurpose() );
        entity.setProjectTime( model.getProjectTime() );

        return entity;
    }

    @Override
    RegistrationTypeModel convertEntity( RegistrationTypeEntity entity ) {
        RegistrationTypeModel model = new RegistrationTypeModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );
        model.setProjectTime( entity.getProjectTime() );

        return model;
    }
}
