package dk.sunepoulsen.timelog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table( name = "holidays" )
public class HolidayEntity {
    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = "holidays_id_seq", sequenceName = "holidays_id_seq", initialValue = 1, allocationSize = 50 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "holidays_id_seq" )
    @Column( name = "holiday_id" )
    private Long id;

    /**
     * Date of the holiday within the year.
     */
    @Column( name = "date", nullable = false )
    private LocalDate date;

    /**
     * Name of the holiday
     */
    @Column( name = "name", nullable = false )
    private String name;
}
