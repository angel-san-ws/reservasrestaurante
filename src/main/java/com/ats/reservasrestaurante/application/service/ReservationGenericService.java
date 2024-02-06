package com.ats.reservasrestaurante.application.service;

import com.ats.reservasrestaurante.domain.dto.ReservationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationGenericService {
    public int createReservation(String numberPersons, String reservationDate, String reservationStartTime);
    public void editReservation(String reservationNumber, String reservationDate, String reservationStartTime);
    public void removeReservation(String reservationNumber);
    public List<ReservationDto> findAllReservations();
}
