package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table( name = "project_accounts" )
@NamedQueries( {
    @NamedQuery( name = "findAllProjectAccounts", query = "SELECT r FROM ProjectAccountEntity r" ),
    @NamedQuery( name = "deleteProjectAccounts", query = "DELETE FROM ProjectAccountEntity r WHERE r.id IN :ids" )
})
public class ProjectAccountEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "project_account_id_seq_generator", sequenceName = "project_account_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "project_account_id_seq_generator" )
    @Column( name = "project_account_id" )
    private Long id;

    @Column( name = "account_number" )
    private String accountNumber;

    @Column( name = "description" )
    private String description;

    @Column( name = "purpose" )
    private String purpose;
}
