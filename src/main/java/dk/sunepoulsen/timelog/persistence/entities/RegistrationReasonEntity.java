package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table( name = "registration_reasons" )
@NamedQueries( {
    @NamedQuery( name = "findAllRegistrationReasons", query = "SELECT r FROM RegistrationReasonEntity r WHERE r.registrationType.id = :registration_type_id" ),
    @NamedQuery( name = "deleteRegistrationReasons", query = "DELETE FROM RegistrationReasonEntity r WHERE r.id IN :ids" )
})
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
}
