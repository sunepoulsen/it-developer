package dk.sunepoulsen.timelog.backend.services;

import dk.sunepoulsen.timelog.backend.events.AccountsEvents;
import dk.sunepoulsen.timelog.backend.events.RegistrationSystemsEvents;
import dk.sunepoulsen.timelog.persistence.entities.AccountEntity;
import dk.sunepoulsen.timelog.persistence.entities.RegistrationSystemEntity;
import dk.sunepoulsen.timelog.persistence.storage.PersistenceStorage;
import dk.sunepoulsen.timelog.ui.model.accounts.AccountModel;
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
public class AccountsService {
    private final RegistrationSystemsEvents registrationSystemsEvents;
    private final AccountsEvents accountsEvents;
    private final PersistenceStorage database;

    public AccountsService( final AccountsEvents accountsEvents, final RegistrationSystemsEvents registrationSystemsEvents,
                            final PersistenceStorage database ) {
        this.accountsEvents = accountsEvents;
        this.registrationSystemsEvents = registrationSystemsEvents;
        this.database = database;
    }

    public AccountModel create( AccountModel accountModel ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( accountModel );

        database.transactional( em -> {
            RegistrationSystemEntity registrationSystemEntity = em.find( RegistrationSystemEntity.class, accountModel.getRegistrationSystem().getId() );
            AccountEntity entity = convertModel( new AccountEntity(), accountModel, registrationSystemEntity );
            em.persist( entity );

            accountModel.setId( entity.getId() );
        } );

        accountsEvents.getCreatedEvent().fire( Arrays.asList( accountModel ) );
        registrationSystemsEvents.getUpdatedEvent().fire( Arrays.asList( accountModel.getRegistrationSystem() ) );

        return accountModel;
    }

    public void delete( List<AccountModel> accounts ) {
        database.transactional( em -> {
            Query q = em.createNamedQuery( "deleteAccounts" );
            q.setParameter( "ids", accounts.stream().map( AccountModel::getId ).collect( Collectors.toList() ) );

            log.info( "Deleted {} account(s)", q.executeUpdate() );
        } );

        log.debug( "Tricker event: AccountEvents::deletedEvent" );
        accountsEvents.getDeletedEvent().fire( accounts );

    }

    public AccountModel update( AccountModel accountModel ) throws TimeLogValidateException {
        TimeLogValidation.validateValue( accountModel );

        database.transactional( em -> {
            RegistrationSystemEntity registrationSystemEntity = em.find( RegistrationSystemEntity.class, accountModel.getRegistrationSystem().getId() );
            AccountEntity entity = em.find( AccountEntity.class, accountModel.getId() );
            entity = convertModel( entity, accountModel, registrationSystemEntity );
            em.persist( entity );

            accountModel.setId( entity.getId() );
        } );

        accountsEvents.getUpdatedEvent().fire( Arrays.asList( accountModel ) );
        registrationSystemsEvents.getUpdatedEvent().fire( Arrays.asList( accountModel.getRegistrationSystem() ) );

        return accountModel;
    }

    public List<AccountModel> findAll() {
        List<AccountEntity> entities = database.query( em ->  em.createNamedQuery( "findAllAccounts", AccountEntity.class ) );

        return entities.stream()
            .map( AccountsService::convertEntity )
            .collect( Collectors.toList() );
    }

    private static AccountEntity convertModel( AccountEntity entity, AccountModel model, RegistrationSystemEntity registrationSystemEntity ) {
        entity.setId( model.getId() );
        entity.setRegistrationSystem( registrationSystemEntity );
        entity.setName( model.getName() );
        entity.setDescription( model.getDescription() );

        return entity;
    }

    private static AccountModel convertEntity( AccountEntity entity ) {
        AccountModel model = new AccountModel();

        model.setId( entity.getId() );
        model.setName( entity.getName() );
        model.setDescription( entity.getDescription() );

        if( entity.getRegistrationSystem() != null ) {
            model.setRegistrationSystem( RegistrationSystemsService.convertEntity( entity.getRegistrationSystem() ) );
        }

        return model;
    }

}
