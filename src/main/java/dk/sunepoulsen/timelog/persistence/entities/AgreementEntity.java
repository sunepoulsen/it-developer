package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table( name = "agreements" )
public class AgreementEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "agreement_id_seq_generator", sequenceName = "agreement_id_seq", allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "agreement_id_seq_generator" )
    @Column( name = "agreement_id" )
    private Long id;

    @Column( name = "name" )
    private String name;

    @Column( name = "start_date" )
    private LocalDate startDate;

    @Column( name = "end_date" )
    private LocalDate endDate;
}
