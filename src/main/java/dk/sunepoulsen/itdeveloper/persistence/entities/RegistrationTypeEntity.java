package dk.sunepoulsen.itdeveloper.persistence.entities;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table( name = "registration_types" )
@NamedQueries( {
    @NamedQuery( name = "findAllRegistrationTypes", query = "SELECT r FROM RegistrationTypeEntity r" ),
    @NamedQuery( name = "deleteRegistrationTypes", query = "DELETE FROM RegistrationTypeEntity r WHERE r.id IN :ids" )
})
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"timeLogs"})
@EqualsAndHashCode(exclude = {"timeLogs"})
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

    @Column( name = "project_time" )
    private Boolean projectTime;

    @OneToMany( cascade = ALL, fetch = FetchType.LAZY, mappedBy = "registrationType" )
    private Set<RegistrationReasonEntity> reasons;

    @OneToMany( cascade = ALL, fetch = FetchType.LAZY, mappedBy = "registrationType" )
    private Set<TimeLogEntity> timeLogs;

}
