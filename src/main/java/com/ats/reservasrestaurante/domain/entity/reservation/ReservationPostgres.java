package com.ats.reservasrestaurante.domain.entity.reservation;

import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"reservation\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"reservationNumber"})})
public class ReservationPostgres {
    @Id
    @SequenceGenerator(
            name = "reserv_id_sequence",
            sequenceName = "reserv_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reserv_id_sequence"
    )
    private Integer id;
    private Integer reservationNumber;
    private Integer numberPersons;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPostgres userPostgres;
    @Column(name = "reservation_start_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime reservationStartDateTime;
    @Column(name = "reservation_end_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime reservationEndDateTime;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TablePostgres tablePostgres;
    @Column(name = "creation_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationTime;
    private Boolean enabled;
}
