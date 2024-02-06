package com.ats.reservasrestaurante.domain.entity.table;

import com.ats.reservasrestaurante.domain.entity.reservation.ReservationPostgres;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"table\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"tableNumber"})})
public class TablePostgres {
    @Id
    @SequenceGenerator(
            name = "table_id_sequence",
            sequenceName = "table_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "table_id_sequence"
    )
    private Integer id;
    private Integer tableNumber;
    private Integer maxClients;
    private Boolean enabled;
    @JsonIgnore
    @OneToMany
    private List<ReservationPostgres> reservationPostgres;
}
