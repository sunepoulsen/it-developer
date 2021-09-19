package dk.sunepoulsen.timelog.persistence.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table( name = "project_accounts" )
@NamedQueries( {
    @NamedQuery( name = "findAllProjectAccounts", query = "SELECT r FROM ProjectAccountEntity r" ),
    @NamedQuery( name = "deleteProjectAccounts", query = "DELETE FROM ProjectAccountEntity r WHERE r.id IN :ids" )
})
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"timeLogs"})
@EqualsAndHashCode(exclude = {"timeLogs"})
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

    @ManyToMany(mappedBy = "projectAccounts", fetch = FetchType.EAGER)
    public Set<TimeLogEntity> timeLogs;
}
