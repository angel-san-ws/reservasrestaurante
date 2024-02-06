package com.ats.reservasrestaurante.domain.repository.reservation;

import com.ats.reservasrestaurante.domain.entity.reservation.ReservationPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepositoryPostgres extends JpaRepository<ReservationPostgres,Integer> {
    Optional<ReservationPostgres> findReservationPostgresByReservationNumber(int reservationNumber);
    Optional<ReservationPostgres> findTopByOrderByReservationNumberDesc();
    List<ReservationPostgres> findAllByEnabledIsTrueAndReservationStartDateTimeGreaterThanEqualAndReservationEndDateTimeLessThanEqual(LocalDateTime reservationStartDateTime, LocalDateTime reservationEndDateTime);
}
