package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.persistence.entities.ProjectAccountEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.ProjectAccountModel;
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
