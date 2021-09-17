package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table( name = "agreements" )
@NamedQueries( {
    @NamedQuery( name = "findAllAgreements", query = "SELECT a FROM AgreementEntity a" ),
    @NamedQuery( name = "findAgreementsByDate", query = "SELECT a FROM AgreementEntity a WHERE a.startDate <= :date AND (a.endDate IS NULL OR :date <= a.endDate)" ),
    @NamedQuery( name = "deleteAgreements", query = "DELETE FROM AgreementEntity a WHERE a.id IN :ids" )
})
public class AgreementEntity implements AbstractEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "agreement_id_seq_generator", sequenceName = "agreement_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "agreement_id_seq_generator" )
    @Column( name = "agreement_id" )
    private Long id;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "start_date", nullable = false )
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
