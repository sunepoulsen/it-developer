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
        entity.setAllDay( model.isAllDay() );

        return entity;
    }

    @Override
    RegistrationTypeModel convertEntity( RegistrationTypeEntity entity ) {
        RegistrationTypeModel model = new RegistrationTypeModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );
        model.setAllDay( entity.getAllDay() );

        return model;
    }
}
