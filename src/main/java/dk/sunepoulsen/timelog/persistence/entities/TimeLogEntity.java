package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table( name = "timelogs" )
@NamedQueries( {
    @NamedQuery( name = "findAllTimeLogs", query = "SELECT t FROM TimeLogEntity t ORDER BY t.date" ),
    @NamedQuery( name = "findByDates", query = "SELECT t FROM TimeLogEntity t WHERE t.date >= :from AND t.date <= :to ORDER BY t.date, t.startTime ASC" ),
    @NamedQuery( name = "deleteTimeLogs", query = "DELETE FROM TimeLogEntity t WHERE t.id IN :ids" )
})
public class TimeLogEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "timelog_id_seq_generator", sequenceName = "timelog_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "timelog_id_seq_generator" )
    @Column( name = "timelog_id" )
    private Long id;

    @Column( name = "timelog_date", columnDefinition="DATE" )
    private LocalDate date;

    @Column( name = "start_time", columnDefinition="TIME" )
    private LocalTime startTime;

    @Column( name = "end_time", columnDefinition="TIME" )
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn( name = "registration_type_id", nullable = false )
    private RegistrationTypeEntity registrationType;

    @ManyToOne
    @JoinColumn( name = "registration_reason_id" )
    private RegistrationReasonEntity registrationReason;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="timelog_project_accounts",
        joinColumns=@JoinColumn(name="timelog_id", referencedColumnName="timelog_id"),
        inverseJoinColumns=@JoinColumn(name="project_account_id", referencedColumnName="project_account_id")
    )
    public Set<ProjectAccountEntity> projectAccounts;
}
