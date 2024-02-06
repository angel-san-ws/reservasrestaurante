package com.ats.reservasrestaurante.domain.entity.reservation;

import com.ats.reservasrestaurante.domain.entity.table.TableMongo;
import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reservation")
public class ReservationMongo {
    @Id
    private String id;
    private Integer reservationNumber;
    private Integer numberPersons;
    @DBRef
    private UserMongo userMongo;
    @DBRef
    private TableMongo tableMongo;
    private LocalDateTime reservationStartDateTime;
    private LocalDateTime reservationEndDateTime;
    private LocalDateTime creationTime;
    private Boolean enabled;
}
