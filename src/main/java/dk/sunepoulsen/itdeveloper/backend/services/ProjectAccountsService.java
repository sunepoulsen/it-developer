package dk.sunepoulsen.itdeveloper.backend.services;

import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel;
import dk.sunepoulsen.itdeveloper.persistence.entities.ProjectAccountEntity;
import dk.sunepoulsen.itdeveloper.persistence.storage.PersistenceStorage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectAccountsService extends AbstractPersistenceService<ProjectAccountModel, ProjectAccountEntity>{
    public ProjectAccountsService(final PersistenceStorage database ) {
        super(database, ProjectAccountEntity.class, "findAllProjectAccounts", "deleteProjectAccounts");
    }

    @Override
    protected ProjectAccountEntity convertModel( ProjectAccountEntity entity, ProjectAccountModel model ) {
        entity.setId( model.getId() );
        entity.setAccountNumber( model.getAccountNumber() );
        entity.setDescription( model.getDescription() );
        entity.setPurpose( model.getPurpose() );

        return entity;
    }

    @Override
    protected ProjectAccountModel convertEntity( ProjectAccountEntity entity ) {
        ProjectAccountModel model = new ProjectAccountModel();

        model.setId( entity.getId() );
        model.setAccountNumber( entity.getAccountNumber() );
        model.setDescription( entity.getDescription() );
        model.setPurpose( entity.getPurpose() );

        return model;
    }
}
