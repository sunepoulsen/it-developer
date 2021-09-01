package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table( name = "timelogs" )
public class TimeLogEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "timelog_id_seq_generator", sequenceName = "timelog_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "timelog_id_seq_generator" )
    @Column( name = "timelog_id" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "account_id", nullable = false )
    private AccountEntity account;

    @Column( name = "all_day" )
    private Boolean allDay;

    @Column( name = "start_time" )
    private LocalDateTime startTime;

    @Column( name = "end_time" )
    private LocalDateTime endTime;

    @Column( name = "description" )
    private String description;
}
