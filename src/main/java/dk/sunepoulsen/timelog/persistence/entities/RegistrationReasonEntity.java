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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table( name = "registration_reasons" )
@NamedQueries( {
    @NamedQuery( name = "findAllRegistrationReasons", query = "SELECT r FROM RegistrationReasonEntity r WHERE r.registrationType.id = :registration_type_id" ),
    @NamedQuery( name = "deleteRegistrationReasons", query = "DELETE FROM RegistrationReasonEntity r WHERE r.id IN :ids" )
})
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"timeLogs"})
@EqualsAndHashCode(exclude = {"timeLogs"})
public class RegistrationReasonEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "registration_reason_id_seq_generator", sequenceName = "registration_reason_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "registration_reason_id_seq_generator" )
    @Column( name = "registration_reason_id" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "registration_type_id", nullable = false )
    private RegistrationTypeEntity registrationType;

    @Column( name = "name" )
    private String name;

    @Column( name = "description" )
    private String description;

    @Column( name = "purpose" )
    private String purpose;

    @OneToMany( cascade = ALL, fetch = FetchType.LAZY, mappedBy = "registrationReason" )
    private Set<TimeLogEntity> timeLogs;
}
