package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table( name = "agreements" )
@NamedQueries( {
    @NamedQuery( name = "findAllAgreements", query = "SELECT r FROM AgreementEntity r" ),
    @NamedQuery( name = "deleteAgreements", query = "DELETE FROM AgreementEntity r WHERE r.id IN :ids" )
})
public class AgreementEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "agreement_id_seq_generator", sequenceName = "agreement_id_seq_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "agreement_id_seq_seq_generator" )
    @Column( name = "agreement_id" )
    private Long id;

    @Column( name = "name" )
    private String name;

    @Column( name = "start_date" )
    private LocalDate startDate;

    @Column( name = "end_date" )
    private LocalDate endDate;

    @Column( name = "monday_norm" )
    private Double mondayNorm;

    @Column( name = "tuesday_norm" )
    private Double tuesdayNorm;

    @Column( name = "wednesday_norm" )
    private Double wednesdayNorm;

    @Column( name = "thursday_norm" )
    private Double thursdayNorm;

    @Column( name = "friday_norm" )
    private Double fridayNorm;

    @Column( name = "saturday_norm" )
    private Double saturdayNorm;

    @Column( name = "sunday_norm" )
    private Double sundayNorm;
}
