package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table( name = "registration_types" )
@NamedQueries( {
    @NamedQuery( name = "findAllRegistrationTypes", query = "SELECT r FROM RegistrationTypeEntity r" ),
    @NamedQuery( name = "deleteRegistrationTypes", query = "DELETE FROM RegistrationTypeEntity r WHERE r.id IN :ids" )
})
public class RegistrationTypeEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "registration_type_id_seq_generator", sequenceName = "registration_type_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "registration_type_id_seq_generator" )
    @Column( name = "registration_type_id" )
    private Long id;

    @Column( name = "name" )
    private String name;

    @Column( name = "description" )
    private String description;

    @Column( name = "purpose" )
    private String purpose;

    @Column( name = "all_day" )
    private Boolean allDay;

    @OneToMany( cascade = ALL, fetch = FetchType.LAZY, mappedBy = "registrationType" )
    private Set<RegistrationReasonEntity> reasons;
}
