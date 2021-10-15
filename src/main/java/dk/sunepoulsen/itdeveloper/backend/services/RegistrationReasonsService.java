package dk.sunepoulsen.itdeveloper.backend.services;

import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationReasonModel;
import dk.sunepoulsen.itdeveloper.persistence.entities.RegistrationReasonEntity;
import dk.sunepoulsen.itdeveloper.persistence.entities.RegistrationTypeEntity;
import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunepoulsen on 12/06/2017.
 */
@Slf4j
public class RegistrationReasonsService extends AbstractPersistenceService<RegistrationReasonModel, RegistrationReasonEntity> {
    private Long registrationTypeId;

    public RegistrationReasonsService(final PersistenceStorage database, Long registrationTypeId ) {
        super(database, RegistrationReasonEntity.class, "findAllRegistrationReasons", "deleteRegistrationReasons");
        this.registrationTypeId = registrationTypeId;
    }

    public List<RegistrationReasonModel> findAll() {
        List<RegistrationReasonEntity> entities = database.query( em ->
            em.createNamedQuery( "findAllRegistrationReasons", RegistrationReasonEntity.class )
            .setParameter("registration_type_id", registrationTypeId)
        );

        return entities.stream()
                .map( this::convertEntity )
                .collect( Collectors.toList() );
    }

    @Override
    protected RegistrationReasonEntity convertModel( RegistrationReasonEntity entity, RegistrationReasonModel model ) {
        entity.setId( model.getId() );

        RegistrationTypeEntity registrationTypeEntity = new RegistrationTypeEntity();
        registrationTypeEntity.setId(model.getRegistrationTypeId());
        entity.setRegistrationType(registrationTypeEntity);

        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );
        entity.setPurpose( model.getPurpose() );

        return entity;
    }

    @Override
    protected RegistrationReasonModel convertEntity( RegistrationReasonEntity entity ) {
        RegistrationReasonModel model = new RegistrationReasonModel();

        model.setId( entity.getId() );
        model.setRegistrationTypeId(entity.getRegistrationType().getId());
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );

        return model;
    }
}
