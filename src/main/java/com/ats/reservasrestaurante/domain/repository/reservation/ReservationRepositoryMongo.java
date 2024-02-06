package com.ats.reservasrestaurante.domain.repository.reservation;

import com.ats.reservasrestaurante.domain.entity.reservation.ReservationMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryMongo extends MongoRepository<ReservationMongo,String> {
    Optional<ReservationMongo> findReservationMongoByReservationNumber(int reservationNumber);
    Optional<ReservationMongo> findTopByOrderByReservationNumberDesc();
    List<ReservationMongo> findAllByEnabledIsTrueAndReservationStartDateTimeGreaterThanEqualAndReservationEndDateTimeLessThanEqual(LocalDateTime reservationStartDateTime, LocalDateTime reservationEndDateTime);
}
