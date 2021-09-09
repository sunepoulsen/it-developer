package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table( name = "timelogs" )
@NamedQueries( {
    @NamedQuery( name = "findAllTimeLogs", query = "SELECT t FROM TimeLogEntity t" ),
    @NamedQuery( name = "findByDates", query = "SELECT t FROM TimeLogEntity t WHERE t.date >= :from AND t.date < :to" ),
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

    @Column( name = "timelog_date" )
    private LocalDate date;

    @Column( name = "start_time" )
    private LocalTime startTime;

    @Column( name = "end_time" )
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn( name = "registration_type_id", nullable = false )
    private RegistrationTypeEntity registrationType;

    @ManyToOne
    @JoinColumn( name = "registration_reason_id" )
    private RegistrationReasonEntity registrationReason;

    @ManyToMany(mappedBy="timeLogs")
    public Set<ProjectAccountEntity> projectAccounts;
}
